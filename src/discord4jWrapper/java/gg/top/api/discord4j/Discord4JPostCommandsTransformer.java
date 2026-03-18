package gg.top.api.discord4j;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.core.JsonProcessingException;

import discord4j.common.JacksonResources;
import discord4j.core.DiscordClient;
import gg.top.api.io.PostCommandsTransformer;

/**
 * A PostCommandsTransformer implementation tailored for Discord4J bots.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class Discord4JPostCommandsTransformer implements PostCommandsTransformer {
  private final DiscordClient client;

  /**
   * Creates a new Discord4J post commands transformer instance.
   *
   * @param client The Discord4J client reference to use.
   * @since 1.0.0
   */
  public Discord4JPostCommandsTransformer(final DiscordClient client) {
    this.client = client;
  }

  /**
   * Retrieves a list of your Discord bot's application commands in the form of Discord API's raw JSON format.
   *
   * @return CompletableFuture&lt;String&gt; A list of your Discord bot's application commands in the form of Discord API's raw JSON format.
   * @since 1.0.0
   */
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
