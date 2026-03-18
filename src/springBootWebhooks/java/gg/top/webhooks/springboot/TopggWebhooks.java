package gg.top.webhooks.springboot;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.Payload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

/**
 * A Spring Boot-based Top.gg webhook manager.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class TopggWebhooks<R> implements TopggWebhookEventListener<R> {
  private static final Logger logger = Logger.getLogger(TopggWebhooks.class.getName());

  private byte[] secret;
  private final ExecutorService executor;
  private final long timeout;
  private final Gson gson;

  /**
   * Creates a new Spring Boot-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param executor The executor service to use to process payload requests concurrently. Defaults to a 100-thread thread pool.
   * @param timeout The timeout for reading payloads in milliseconds. Defaults to five seconds.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final ExecutorService executor, final long timeout) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.executor = executor;
    this.timeout = timeout;
    gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .create();
  }

  /**
   * Creates a new Spring Boot-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param executor The executor service to use to process payload requests concurrently. Defaults to a 100-thread thread pool.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final ExecutorService executor) {
    this(secret, executor, 5000L);
  }

  /**
   * Creates a new Spring Boot-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param timeout The timeout for reading payloads in milliseconds. Defaults to five seconds.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final long timeout) {
    this(secret, Executors.newFixedThreadPool(100), timeout);
  }

  /**
   * Creates a new Spring Boot-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret) {
    this(secret, 5000L);
  }

  /**
   * Retrieves the secret used to authorize external requests.
   *
   * @return String The secret used to authorize external requests.
   * @since 1.0.0
   */
  public String getSecret() {
    return new String(secret, StandardCharsets.UTF_8);
  }

  /**
   * Sets the secret to use to authorize external requests.
   *
   * @param newSecret The new secret to use to authorize external requests.
   * @since 1.0.0
   */
  public void setSecret(final String newSecret) {
    secret = newSecret.getBytes(StandardCharsets.UTF_8);
  }

  @SuppressWarnings("UseSpecificCatch")
  private ResponseEntity<R> dispatchSync(final String body, final String signatureHeader, final String trace) {
    try {
      final HashMap<String, String> parsedSignature =
          Arrays.stream(signatureHeader.split(","))
              .map(part -> part.split("=", 2))
              .collect(
                  Collectors.toMap(
                      part -> part[0].trim(),
                      part -> part[1].trim(),
                      (existing, replacement) -> replacement,
                      HashMap::new));

      final String signature = parsedSignature.get("v1");
      final String timestamp = parsedSignature.get("t");

      assert signature != null && timestamp != null;

      final SecretKeySpec key = new SecretKeySpec(secret, "HmacSHA256");
      final Mac hmac = Mac.getInstance("HmacSHA256");

      hmac.init(key);

      final byte[] digest =
          hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

      if (!signature.equals(HexFormat.of().formatHex(digest))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      final Payload payload = gson.fromJson(body, Payload.class);

      try {
        return switch (payload.getType()) {
          case "integration.create" ->
              onIntegrationCreate(payload.getData(gson, IntegrationCreatePayload.class), trace);
          case "integration.delete" ->
              onIntegrationDelete(payload.getData(gson, IntegrationDeletePayload.class), trace);
          case "webhook.test" -> onTest(payload.getData(gson, TestPayload.class), trace);
          case "vote.create" -> onVoteCreate(payload.getData(gson, VoteCreatePayload.class), trace);
          default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        };
      } catch (final Throwable ignored) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    } catch (final JsonSyntaxException error) {
      logger.warning(String.format("Unable to parse Top.gg webhook payload. Please report this bug to the SDK maintainers.\nCause: %s\n--- BEGIN BODY DUMP ---\n%s\n--- END BODY DUMP ---", error.getMessage(), body));

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (final ArrayIndexOutOfBoundsException | AssertionError | JsonIOException ignored) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (final Throwable ignored) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Tries to process a payload request and dispatch it to the listeners.
   *
   * @param body The HTTP request body that comes from @RequestBody.
   * @param signatureHeader The HTTP request header that comes from @RequestHeader("x-topgg-signature"). Used to verify requests.
   * @param trace The HTTP request header that comes from @RequestHeader("x-topgg-trace"). Used to debug and correlate requests with Top.gg support.
   * @return DeferredResult&lt;ResponseEntity&lt;R&gt;&gt; The deferred response for this request.
   * @since 1.0.0
   */
  protected DeferredResult<ResponseEntity<R>> dispatch(final String body, final String signatureHeader, final String trace) {
    final DeferredResult<ResponseEntity<R>> result = new DeferredResult<>(
      timeout, ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build()
    );

    executor.submit(() -> result.setResult(dispatchSync(body, signatureHeader, trace)));

    return result;
  }
}
