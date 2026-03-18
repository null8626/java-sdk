package gg.top.webhooks.eclipsejetty;

import gg.top.webhooks.Mocks;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TopggEclipseJettyWebhookTests {
  private static Server SERVER = null;

  private static final String SECRET = System.getenv("TOPGG_WEBHOOK_SECRET");
  private static final String TRACE = "trace";
  private static Mocks MOCKS;

  @BeforeAll
  public static void setup() throws IOException, NullPointerException, Exception {
    MOCKS = new Mocks();

    SERVER = new Server(8080);

    final ServletContextHandler context = new ServletContextHandler();

    context.setContextPath("/");
    context.addServlet(new ServletHolder(new CustomWebhooks()), "/webhook");

    SERVER.setHandler(context);
    SERVER.start();
  }

  private void send(final String name, final String payload)
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    final HttpURLConnection connection =
        (HttpURLConnection) URI.create("http://localhost:8080/webhook").toURL().openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("x-topgg-signature", Mocks.signature(SECRET, payload));
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
  public void integrationCreate()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("integrationCreate", MOCKS.integrationCreatePayload);
  }

  @Test
  public void integrationDelete()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("integrationDelete", MOCKS.integrationDeletePayload);
  }

  @Test
  public void test()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("test", MOCKS.testPayload);
  }

  @Test
  public void voteCreate()
      throws NoSuchAlgorithmException, InvalidKeyException, ProtocolException, IOException {
    send("voteCreate", MOCKS.voteCreatePayload);
  }

  @AfterAll
  public static void cleanup() throws Exception {
    if (SERVER != null) {
      SERVER.stop();
    }
  }
}
