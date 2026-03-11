package gg.top.webhooks.springboot;

import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface TopggWebhookEventListener<R> {
  default ResponseEntity<R> onIntegrationCreate(
      final IntegrationCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  default ResponseEntity<R> onIntegrationDelete(
      final IntegrationDeletePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  default ResponseEntity<R> onTest(final TestPayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  default ResponseEntity<R> onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
