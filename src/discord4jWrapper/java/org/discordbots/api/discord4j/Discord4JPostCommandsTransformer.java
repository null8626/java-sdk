package org.discordbots.api.discord4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import discord4j.common.JacksonResources;
import discord4j.core.DiscordClient;
import java.util.concurrent.CompletionStage;
import org.discordbots.api.client.io.PostCommandsTransformer;

public class Discord4JPostCommandsTransformer implements PostCommandsTransformer {
  private final DiscordClient client;

  public Discord4JPostCommandsTransformer(final DiscordClient client) {
    this.client = client;
  }

  @Override
  public CompletionStage<String> toJsonString() {
    return this.client
        .getApplicationId()
        .toFuture()
        .thenCompose(
            applicationId ->
                this.client
                    .getApplicationService()
                    .getGlobalApplicationCommands(applicationId)
                    .collectList()
                    .toFuture())
        .thenApply(
            commands -> {
              try {
                return JacksonResources.create().getObjectMapper().writeValueAsString(commands);
              } catch (final JsonProcessingException ignored) {
                return "[]";
              }
            });
  }
}
