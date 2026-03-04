package org.discordbots.webhooks.eclipsejetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.discordbots.webhooks.MockPayloads;
import org.discordbots.webhooks.MockSignature;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DBLEclipseJettyWebhooksTest {
  private static Server SERVER = null;

  private static final String SECRET = System.getenv("TOPGG_WEBHOOK_SECRET");
  private static final String TRACE = "trace";
  private static MockPayloads MOCK_PAYLOADS;

  @BeforeAll
  public static void setup() throws IOException, NullPointerException, Exception {
    MOCK_PAYLOADS = new MockPayloads();

    SERVER = new Server(8080);

    final ServletContextHandler context = new ServletContextHandler();

    context.setContextPath("/");
    context.addServlet(new ServletHolder(new CustomWebhooks()), "/webhook");

    SERVER.setHandler(context);
    SERVER.start();
  }

  private void send(final String name, final String payload)
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    final MockSignature signature = new MockSignature(SECRET, payload);

    final HttpURLConnection connection =
        (HttpURLConnection) URI.create("http://localhost:8080/webhook").toURL().openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("x-topgg-signature", signature.getSignatureHeader());
    connection.setRequestProperty("x-topgg-trace", TRACE);
    connection.setDoOutput(true);

    try (final OutputStream outputStream = connection.getOutputStream()) {
      final byte[] payloadBytes = payload.getBytes("utf-8");

      outputStream.write(payloadBytes, 0, payloadBytes.length);
    }

    Assertions.assertEquals(200, connection.getResponseCode());

    try (final BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
      final StringBuilder response = new StringBuilder();
      String responseLine;

      while ((responseLine = reader.readLine()) != null) {
        response.append(responseLine.trim());
      }

      Assertions.assertEquals("ej:" + name + "," + TRACE, response.toString());
    }
  }

  @Test
  void integrationCreate()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("integrationCreate", MOCK_PAYLOADS.integrationCreate);
  }

  @Test
  void integrationDelete()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("integrationDelete", MOCK_PAYLOADS.integrationDelete);
  }

  @Test
  void test() throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("test", MOCK_PAYLOADS.test);
  }

  @Test
  void voteCreate()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("voteCreate", MOCK_PAYLOADS.voteCreate);
  }

  @AfterAll
  public static void cleanup() throws Exception {
    if (SERVER != null) {
      SERVER.stop();
    }
  }
}
