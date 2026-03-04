package org.discordbots.webhooks.dropwizard;

import io.dropwizard.core.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.discordbots.webhooks.MockPayloads;
import org.discordbots.webhooks.MockSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DBLWebhooksTest {
  private static final DropwizardAppExtension<Configuration> APP =
      new DropwizardAppExtension<>(
          CustomServer.class, ResourceHelpers.resourceFilePath("dropwizard-test-config.yml"));
  private static final String SECRET = System.getenv("TOPGG_WEBHOOK_SECRET");
  private static final String TRACE = "trace";
  private static MockPayloads MOCK_PAYLOADS;

  @BeforeAll
  public static void setup() throws IOException, NullPointerException {
    MOCK_PAYLOADS = new MockPayloads();
  }

  private void send(final String name, final String payload)
      throws NoSuchAlgorithmException, InvalidKeyException {
    final MockSignature signature = new MockSignature(SECRET, payload);

    final Response response =
        APP.client()
            .target(String.format("http://localhost:%d/webhook", APP.getLocalPort()))
            .request()
            .header("Content-Type", "application/json")
            .header("x-topgg-signature", signature.getSignatureHeader())
            .header("x-topgg-trace", TRACE)
            .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertEquals(name + "," + TRACE, response.readEntity(String.class));
  }

  @Test
  void integrationCreate() throws NoSuchAlgorithmException, InvalidKeyException {
    send("integrationCreate", MOCK_PAYLOADS.integrationCreate);
  }

  @Test
  void integrationDelete() throws NoSuchAlgorithmException, InvalidKeyException {
    send("integrationDelete", MOCK_PAYLOADS.integrationDelete);
  }

  @Test
  void test() throws NoSuchAlgorithmException, InvalidKeyException {
    send("test", MOCK_PAYLOADS.test);
  }

  @Test
  void voteCreate() throws NoSuchAlgorithmException, InvalidKeyException {
    send("voteCreate", MOCK_PAYLOADS.voteCreate);
  }
}
