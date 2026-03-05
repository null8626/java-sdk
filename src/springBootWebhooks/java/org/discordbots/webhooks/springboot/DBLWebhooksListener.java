package org.discordbots.webhooks.springboot;

import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DBLWebhooksListener<R> {
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
