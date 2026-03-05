package org.discordbots.webhooks.eclipsejetty;

import jakarta.servlet.http.HttpServletResponse;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

public interface DBLWebhooksListener {
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
