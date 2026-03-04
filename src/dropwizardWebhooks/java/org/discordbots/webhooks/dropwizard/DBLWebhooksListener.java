package org.discordbots.webhooks.dropwizard;

import jakarta.ws.rs.core.Response;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

public interface DBLWebhooksListener {
  default Response onIntegrationCreate(IntegrationCreatePayload payload, String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onIntegrationDelete(IntegrationDeletePayload payload, String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onTest(TestPayload payload, String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onVoteCreate(VoteCreatePayload payload, String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
