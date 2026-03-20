package gg.top.webhooks.dropwizard;

import gg.top.webhooks.Mocks;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TopggDropwizardWebhookTests {
  private static final DropwizardAppExtension<Configuration> APP =
      new DropwizardAppExtension<>(
          CustomServer.class, ResourceHelpers.resourceFilePath("dropwizard-test-config.yml"));

  private static Mocks MOCKS;

  @BeforeAll
  public static void setup() throws IOException, NullPointerException {
    MOCKS = new Mocks();
  }

  private void send(final String name, final String payload)
      throws NoSuchAlgorithmException, InvalidKeyException {
    final Response response =
        APP.client()
            .target(String.format("http://localhost:%d/webhook", APP.getLocalPort()))
            .request()
            .header("Content-Type", "application/json")
            .header("x-topgg-signature", Mocks.signature(payload))
            .header("x-topgg-trace", Mocks.TRACE)
            .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertEquals("dw:" + name + "," + Mocks.TRACE, response.readEntity(String.class));
  }

  @Test
  public void integrationCreate() throws NoSuchAlgorithmException, InvalidKeyException {
    send("integrationCreate", MOCKS.integrationCreatePayload);
  }

  @Test
  public void integrationDelete() throws NoSuchAlgorithmException, InvalidKeyException {
    send("integrationDelete", MOCKS.integrationDeletePayload);
  }

  @Test
  public void test() throws NoSuchAlgorithmException, InvalidKeyException {
    send("test", MOCKS.testPayload);
  }

  @Test
  public void voteCreate() throws NoSuchAlgorithmException, InvalidKeyException {
    send("voteCreate", MOCKS.voteCreatePayload);
  }
}
