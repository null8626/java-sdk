package gg.top.webhooks.springboot;

import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A Spring Boot-based Top.gg webhook event listener.
 *
 * @param <R> The response entity.
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TopggWebhookEventListener<R> {
  /**
   * Fires when a user has connected to your webhook integration.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @return ResponseEntity&lt;R&gt; The response for this request.
   * @since 1.0.0
   */
  default ResponseEntity<R> onIntegrationCreate(
      final IntegrationCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Fires when a user has disconnected from your webhook integration.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @return ResponseEntity&lt;R&gt; The response for this request.
   * @since 1.0.0
   */
  default ResponseEntity<R> onIntegrationDelete(
      final IntegrationDeletePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Fires when a test webhook was sent from the dashboard.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @return ResponseEntity&lt;R&gt; The response for this request.
   * @since 1.0.0
   */
  default ResponseEntity<R> onTest(final TestPayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Fires when a user votes for your project.
   *
   * @param payload The webhook payload.
   * @param trace The payload's x-topgg-trace header for debugging and correlating requests with
   *     Top.gg support.
   * @return ResponseEntity&lt;R&gt; The response for this request.
   * @since 1.0.0
   */
  default ResponseEntity<R> onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
