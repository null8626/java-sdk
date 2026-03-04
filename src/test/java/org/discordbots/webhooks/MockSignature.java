package org.discordbots.webhooks;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MockSignature {
  private final long timestamp;
  private final String signature;

  public MockSignature(final String secret, final String body)
      throws NoSuchAlgorithmException, InvalidKeyException {
    timestamp = Instant.now().getEpochSecond();

    final SecretKeySpec key =
        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    final Mac hmac = Mac.getInstance("HmacSHA256");

    hmac.init(key);

    final byte[] digest =
        hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

    signature = HexFormat.of().formatHex(digest);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getSignatureHeader() {
    return "t=" + Long.toString(timestamp) + ",v1=" + signature;
  }
}
