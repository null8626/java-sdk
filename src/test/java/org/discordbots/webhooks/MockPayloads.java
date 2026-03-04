package org.discordbots.webhooks;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MockPayloads {
  public final String integrationCreate;
  public final String integrationDelete;
  public final String test;
  public final String voteCreate;

  public MockPayloads() throws IOException, NullPointerException {
    integrationCreate = read("IntegrationCreate");
    integrationDelete = read("IntegrationDelete");
    test = read("Test");
    voteCreate = read("VoteCreate");
  }

  private static String read(final String name) throws IOException, NullPointerException {
    final InputStream inputStream =
        MockPayloads.class.getResourceAsStream("/" + name + "Payload.json");

    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
  }
}
