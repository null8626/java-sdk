package org.discordbots.webhooks.dropwizard;

import jakarta.ws.rs.core.Response;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

public interface DBLWebhooksListener {
  default Response onIntegrationCreate(final IntegrationCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onIntegrationDelete(final IntegrationDeletePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onTest(final TestPayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  default Response onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
