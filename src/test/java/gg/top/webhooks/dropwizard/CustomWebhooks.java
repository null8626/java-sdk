package gg.top.webhooks.dropwizard;

import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/webhook")
public class CustomWebhooks extends TopggWebhooks {
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
