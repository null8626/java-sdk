package org.discordbots.webhooks;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Mocks {
  public final String integrationCreatePayload;
  public final String integrationDeletePayload;
  public final String testPayload;
  public final String voteCreatePayload;

  public Mocks() throws IOException, NullPointerException {
    integrationCreatePayload = read("IntegrationCreate");
    integrationDeletePayload = read("IntegrationDelete");
    testPayload = read("Test");
    voteCreatePayload = read("VoteCreate");
  }

  private static String read(final String name) throws IOException, NullPointerException {
    final InputStream inputStream = Mocks.class.getResourceAsStream("/" + name + "Payload.json");

    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
  }

  public static String signature(final String secret, final String body)
      throws NoSuchAlgorithmException, InvalidKeyException {
    final long timestamp = Instant.now().getEpochSecond();

    final SecretKeySpec key =
        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    final Mac hmac = Mac.getInstance("HmacSHA256");

    hmac.init(key);

    final byte[] digest =
        hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

    return "t=" + Long.toString(timestamp) + ",v1=" + HexFormat.of().formatHex(digest);
  }
}
