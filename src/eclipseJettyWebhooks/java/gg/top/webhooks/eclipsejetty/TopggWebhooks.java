package gg.top.webhooks.eclipsejetty;

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
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * An Eclipse Jetty-based Top.gg webhook manager.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class TopggWebhooks extends HttpServlet implements TopggWebhookEventListener {
  private static final Logger logger = Logger.getLogger(TopggWebhooks.class.getName());

  private byte[] secret;
  private final ExecutorService executor;
  private final long timeout;
  private final Gson gson;

  /**
   * Creates a new Eclipse Jetty-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param executor The executor service to use to process payload requests concurrently. Defaults
   *     to a 100-fixed thread pool.
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
   * Creates a new Eclipse Jetty-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param executor The executor service to use to process payload requests concurrently. Defaults
   *     to a 100-fixed thread pool.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final ExecutorService executor) {
    this(secret, executor, 5000L);
  }

  /**
   * Creates a new Eclipse Jetty-based webhook manager instance.
   *
   * @param secret The secret to use to authorize external requests.
   * @param timeout The timeout for reading payloads in milliseconds. Defaults to five seconds.
   * @since 1.0.0
   */
  public TopggWebhooks(final String secret, final long timeout) {
    this(secret, Executors.newFixedThreadPool(100), timeout);
  }

  /**
   * Creates a new Eclipse Jetty-based webhook manager instance.
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
  private void dispatch(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
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
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");

        return;
      }

      final Payload payload = gson.fromJson(body, Payload.class);
      final String trace = request.getHeader("x-topgg-trace");

      try {
        switch (payload.getType()) {
          case "integration.create" ->
              onIntegrationCreate(
                  response, payload.getData(gson, IntegrationCreatePayload.class), trace);
          case "integration.delete" ->
              onIntegrationDelete(
                  response, payload.getData(gson, IntegrationDeletePayload.class), trace);
          case "webhook.test" -> onTest(response, payload.getData(gson, TestPayload.class), trace);
          case "vote.create" ->
              onVoteCreate(response, payload.getData(gson, VoteCreatePayload.class), trace);
          default -> {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Bad Request");
          }
        }

        return;
      } catch (final Throwable ignored) {
      }
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

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);

      return;
    } catch (final NoSuchAlgorithmException | InvalidKeyException error) {
      throw new ServletException("Unable to find an HMAC SHA-256 algorithm", error);
    } catch (final ArrayIndexOutOfBoundsException | AssertionError | JsonIOException ignored) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Bad Request");

      return;
    } catch (final Throwable ignored) {
    }

    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.getWriter().write("Internal Server Error");
  }

  /**
   * Tries to process a payload request and dispatch it to the listeners.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException Unable to write an HTTP response.
   * @throws ServletException Unable to find an HMAC SHA-256 algorithm.
   * @since 1.0.0
   */
  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
    final AsyncContext context = request.startAsync();

    context.setTimeout(timeout);

    context.addListener(
        new AsyncListener() {
          @Override
          public void onStartAsync(final AsyncEvent event) {}

          @Override
          public void onError(final AsyncEvent event) throws IOException {
            final HttpServletResponse eventResponse =
                (HttpServletResponse) event.getAsyncContext().getResponse();

            eventResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            eventResponse.getWriter().write("Internal Server Error");

            event.getAsyncContext().complete();
          }

          @Override
          public void onComplete(final AsyncEvent event) {}

          @Override
          public void onTimeout(final AsyncEvent event) throws IOException {
            final HttpServletResponse eventResponse =
                (HttpServletResponse) event.getAsyncContext().getResponse();

            eventResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            eventResponse.getWriter().write("Request timed out");

            event.getAsyncContext().complete();
          }
        });

    executor.submit(
        () -> {
          try {
            dispatch(request, response);
          } catch (final IOException | ServletException error) {
            logger.log(
                Level.SEVERE,
                String.format("Unable to process payload request: %s", error.getMessage()));
          } finally {
            context.complete();
          }
        });
  }
}
