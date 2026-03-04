package org.discordbots.webhooks.dropwizard;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.discordbots.webhooks.payload.IntegrationCreatePayload;
import org.discordbots.webhooks.payload.IntegrationDeletePayload;
import org.discordbots.webhooks.payload.TestPayload;
import org.discordbots.webhooks.payload.VoteCreatePayload;

@Path("/webhook")
public class CustomWebhooks extends DBLWebhooks {
  public CustomWebhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  @Override
  public Response onIntegrationCreate(final IntegrationCreatePayload payload, final String trace) {
    return Response.status(Response.Status.OK).entity("dw:integrationCreate," + trace).build();
  }

  @Override
  public Response onIntegrationDelete(final IntegrationDeletePayload payload, final String trace) {
    return Response.status(Response.Status.OK).entity("dw:integrationDelete," + trace).build();
  }

  @Override
  public Response onTest(final TestPayload payload, final String trace) {
    return Response.status(Response.Status.OK).entity("dw:test," + trace).build();
  }

  @Override
  public Response onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return Response.status(Response.Status.OK).entity("dw:voteCreate," + trace).build();
  }
}
