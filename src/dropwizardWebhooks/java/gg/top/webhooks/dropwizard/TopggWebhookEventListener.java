package gg.top.webhooks.dropwizard;

import jakarta.ws.rs.core.Response;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

public interface TopggWebhookEventListener {
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
