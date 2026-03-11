package gg.top.webhooks.eclipsejetty;

import jakarta.servlet.http.HttpServletResponse;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

public interface TopggWebhookEventListener {
  default void onIntegrationCreate(
      final HttpServletResponse response,
      final IntegrationCreatePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onIntegrationDelete(
      final HttpServletResponse response,
      final IntegrationDeletePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onTest(
      final HttpServletResponse response, final TestPayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onVoteCreate(
      final HttpServletResponse response, final VoteCreatePayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
