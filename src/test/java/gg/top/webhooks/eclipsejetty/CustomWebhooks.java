package gg.top.webhooks.eclipsejetty;

import gg.top.webhooks.Mocks;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomWebhooks extends TopggWebhooks {
  public CustomWebhooks() {
    super(Mocks.SECRET);
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
