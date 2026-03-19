package gg.top.webhooks.dropwizard;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.Payload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * A Dropwizard-based Top.gg webhook manager.
 *
 * @see TopggWebhookEventListener
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class TopggWebhooks implements TopggWebhookEventListener {
  private static final Logger logger = Logger.getLogger(TopggWebhooks.class.getName());

  private byte[] secret;
  private final ExecutorService executor;
  private long timeoutValue;
  private TimeUnit timeoutUnit;
  private final Gson gson;

  /**
   * Creates a new Dropwizard-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param executor The executor service to use to process payload requests concurrently. Defaults
   *     to a 100-fixed thread pool.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final ExecutorService executor) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.executor = executor;

    timeoutValue = 5;
    timeoutUnit = TimeUnit.SECONDS;

    gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .create();
  }

  /**
   * Creates a new Dropwizard-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret) {
    this(secret, Executors.newFixedThreadPool(100));
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

  /**
   * Sets the timeout for reading payloads.
   *
   * @param value The timeout duration value.
   * @param unit The timeout duration unit.
   * @since 1.0.0
   */
  public void setTimeout(final long value, final TimeUnit unit) {
    timeoutValue = value;
    timeoutUnit = unit;
  }

  @SuppressWarnings("UseSpecificCatch")
  private Response dispatch(final HttpServletRequest request) {
    String body = "";

    try {
      final String signatureHeader = request.getHeader("x-topgg-signature");

      assert signatureHeader != null;

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

      body =
          new String(
              ByteStreams.limit(request.getInputStream(), 2 * 1024 * 1024).readAllBytes(),
              StandardCharsets.UTF_8);
      final byte[] digest =
          hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

      if (!signature.equals(HexFormat.of().formatHex(digest))) {
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity("Invalid Authorization")
            .build();
      }

      final Payload payload = gson.fromJson(body, Payload.class);
      final String trace = request.getHeader("x-topgg-trace");

      try {
        return switch (payload.getType()) {
          case "integration.create" ->
              onIntegrationCreate(payload.getData(gson, IntegrationCreatePayload.class), trace);
          case "integration.delete" ->
              onIntegrationDelete(payload.getData(gson, IntegrationDeletePayload.class), trace);
          case "webhook.test" -> onTest(payload.getData(gson, TestPayload.class), trace);
          case "vote.create" -> onVoteCreate(payload.getData(gson, VoteCreatePayload.class), trace);
          default -> Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
        };
      } catch (final Throwable ignored) {
      }
    } catch (final NoSuchAlgorithmException | InvalidKeyException error) {
      throw new WebApplicationException("Unable to find an HMAC SHA-256 algorithm", error);
    } catch (final JsonSyntaxException error) {
      logger.warning(
          String.format(
              "Unable to parse Top.gg webhook payload. Please report this bug to the SDK"
                  + " maintainers.\n"
                  + "Cause: %s\n"
                  + "--- BEGIN BODY DUMP ---\n"
                  + "%s\n"
                  + "--- END BODY DUMP ---",
              error.getMessage(), body));

      return Response.status(Response.Status.NO_CONTENT).build();
    } catch (final ArrayIndexOutOfBoundsException
        | AssertionError
        | JsonIOException
        | IOException ignored) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    } catch (final Throwable ignored) {
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("Internal Server Error")
        .build();
  }

  /**
   * Tries to process a payload request and dispatch it to the listeners.
   *
   * @param request The HTTP request.
   * @param response The asynchronous HTTP response.
   * @throws WebApplicationException Unable to find an HMAC SHA-256 algorithm.
   * @since 1.0.0
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void dispatch(
      @Context final HttpServletRequest request, @Suspended final AsyncResponse response)
      throws WebApplicationException {
    response.setTimeout(timeoutValue, timeoutUnit);
    response.setTimeoutHandler(
        response2 ->
            response2.resume(
                Response.status(Response.Status.REQUEST_TIMEOUT)
                    .entity("Request timed out")
                    .build()));

    executor.submit(() -> response.resume(dispatch(request)));
  }
}
