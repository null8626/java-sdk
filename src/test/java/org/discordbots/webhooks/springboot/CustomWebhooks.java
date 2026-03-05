package org.discordbots.webhooks.springboot;

import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomWebhooks extends DBLWebhooks<String> {
  public CustomWebhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  @PostMapping("/webhook")
  public ResponseEntity<String> main(
      @RequestBody final String body,
      @RequestHeader("x-topgg-signature") final String signature,
      @RequestHeader("x-topgg-trace") final String trace) {
    return dispatch(body, signature, trace);
  }

  @Override
  public ResponseEntity<String> onIntegrationCreate(
      final IntegrationCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.OK).body("sb:integrationCreate," + trace);
  }

  @Override
  public ResponseEntity<String> onIntegrationDelete(
      final IntegrationDeletePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.OK).body("sb:integrationDelete," + trace);
  }

  @Override
  public ResponseEntity<String> onTest(final TestPayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.OK).body("sb:test," + trace);
  }

  @Override
  public ResponseEntity<String> onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.OK).body("sb:voteCreate," + trace);
  }
}
