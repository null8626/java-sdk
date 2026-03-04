package org.discordbots.webhooks.springboot;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.Payload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;
import org.springframework.web.filter.OncePerRequestFilter;

public class DBLWebhooks extends OncePerRequestFilter implements DBLWebhooksListener {
  private byte[] secret;
  private final Gson gson;

  public DBLWebhooks(final String secret) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .create();
  }

  public String getSecret() {
    return new String(secret, StandardCharsets.UTF_8);
  }

  public void setSecret(final String newSecret) {
    secret = newSecret.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  @SuppressWarnings("UseSpecificCatch")
  protected void doFilterInternal(
      HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
      throws IOException, ServletException {
    if (request.getMethod().equalsIgnoreCase("POST")) {
      final String signatureHeader = request.getHeader("x-topgg-signature");

      if (signatureHeader != null) {
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

          final String body =
              new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
          final byte[] digest =
              hmac.doFinal(
                  String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

          if (!signature.equals(HexFormat.of().formatHex(digest))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Authorization");

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
              case "webhook.test" ->
                  onTest(response, payload.getData(gson, TestPayload.class), trace);
              case "vote.create" ->
                  onVoteCreate(response, payload.getData(gson, VoteCreatePayload.class), trace);
            }
          } catch (final Throwable ignored) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
          }
        } catch (final NoSuchAlgorithmException
            | InvalidKeyException
            | ArrayIndexOutOfBoundsException
            | AssertionError
            | JsonSyntaxException
            | JsonIOException
            | IOException error) {
          if (error instanceof NoSuchAlgorithmException || error instanceof InvalidKeyException) {
            throw new ServletException("Unable to find HMAC SHA-256 algorithm", error);
          } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Bad Request");
          }
        }

        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
