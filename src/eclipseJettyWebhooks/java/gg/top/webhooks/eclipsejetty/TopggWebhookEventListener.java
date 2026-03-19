package gg.top.webhooks.eclipsejetty;

import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import jakarta.servlet.http.HttpServletResponse;

/**
 * An Eclipse Jetty-based Top.gg webhook event listener.
 *
 * @see TopggWebhooks
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TopggWebhookEventListener {
  /**
   * Fires when a user has connected to your webhook integration.
   *
   * @param response The response for this request.
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @since 1.0.0
   */
  default void onIntegrationCreate(
      final HttpServletResponse response,
      final IntegrationCreatePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  /**
   * Fires when a user has disconnected from your webhook integration.
   *
   * @param response The response for this request.
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @since 1.0.0
   */
  default void onIntegrationDelete(
      final HttpServletResponse response,
      final IntegrationDeletePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  /**
   * Fires when a test webhook was sent from the dashboard.
   *
   * @param response The response for this request.
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @since 1.0.0
   */
  default void onTest(
      final HttpServletResponse response, final TestPayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  /**
   * Fires when a user votes for your project.
   *
   * @param response The response for this request.
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @since 1.0.0
   */
  default void onVoteCreate(
      final HttpServletResponse response, final VoteCreatePayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
