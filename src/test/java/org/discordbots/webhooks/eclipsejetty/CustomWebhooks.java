package org.discordbots.webhooks.eclipsejetty;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

public class CustomWebhooks extends DBLWebhooks {
  public CustomWebhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  private void reply(final String name, final HttpServletResponse response, final String trace) {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("ej:" + name + "," + trace);
    } catch (final IOException ignored) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void onIntegrationCreate(
      final HttpServletResponse response,
      final IntegrationCreatePayload payload,
      final String trace) {
    reply("integrationCreate", response, trace);
  }

  @Override
  public void onIntegrationDelete(
      final HttpServletResponse response,
      final IntegrationDeletePayload payload,
      final String trace) {
    reply("integrationDelete", response, trace);
  }

  @Override
  public void onTest(
      final HttpServletResponse response, final TestPayload payload, final String trace) {
    reply("test", response, trace);
  }

  @Override
  public void onVoteCreate(
      final HttpServletResponse response, final VoteCreatePayload payload, final String trace) {
    reply("voteCreate", response, trace);
  }
}
