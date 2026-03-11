package gg.top.webhooks.springboot;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

public class TopggWebhooks<R> implements TopggWebhookEventListener<R> {
  private static final Logger logger = Logger.getLogger(TopggWebhooks.class.getName());

  private byte[] secret;
  private final Gson gson;

  public TopggWebhooks(final String secret) {
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

  @SuppressWarnings("UseSpecificCatch")
  protected ResponseEntity<R> dispatch(
      final String body, final String signatureHeader, final String trace) {
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
    } catch (final NoSuchAlgorithmException
        | InvalidKeyException
        | ArrayIndexOutOfBoundsException
        | AssertionError
        | JsonSyntaxException
        | JsonIOException error) {
      if (error instanceof JsonSyntaxException) {
        logger.warning(String.format("Unable to parse Top.gg webhook payload. Please report this bug to the SDK maintainers.\nCause: %s\n--- BEGIN BODY DUMP ---\n%s\n--- END BODY DUMP ---", error.getMessage(), body));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }

      return ResponseEntity.status(
              (error instanceof NoSuchAlgorithmException || error instanceof InvalidKeyException)
                  ? HttpStatus.INTERNAL_SERVER_ERROR
                  : HttpStatus.BAD_REQUEST)
          .build();
    }
  }
}
