package gg.top.webhooks.dropwizard;

import jakarta.ws.rs.core.Response;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

/**
 * A Dropwizard-based Top.gg webhook event listener.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TopggWebhookEventListener {
  /**
   * Fires when a user has connected to your webhook integration.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with Top.gg support.
   * @return Response The response for this request.
   * @since 1.0.0
   */
  default Response onIntegrationCreate(final IntegrationCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  /**
   * Fires when a user has disconnected from your webhook integration.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with Top.gg support.
   * @return Response The response for this request.
   * @since 1.0.0
   */
  default Response onIntegrationDelete(final IntegrationDeletePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  /**
   * Fires when a test webhook was sent from the dashboard.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with Top.gg support.
   * @return Response The response for this request.
   * @since 1.0.0
   */
  default Response onTest(final TestPayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  /**
   * Fires when a user votes for your project.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with Top.gg support.
   * @return Response The response for this request.
   * @since 1.0.0
   */
  default Response onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
