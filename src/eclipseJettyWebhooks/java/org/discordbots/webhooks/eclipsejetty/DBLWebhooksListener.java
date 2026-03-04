package org.discordbots.webhooks.eclipsejetty;

import jakarta.servlet.http.HttpServletResponse;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

public interface DBLWebhooksListener {
  default void onIntegrationCreate(
      HttpServletResponse response, IntegrationCreatePayload payload, String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onIntegrationDelete(
      HttpServletResponse response, IntegrationDeletePayload payload, String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onTest(HttpServletResponse response, TestPayload payload, String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  default void onVoteCreate(HttpServletResponse response, VoteCreatePayload payload, String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
