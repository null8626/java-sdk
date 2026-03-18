package gg.top.api.io;

import com.google.gson.JsonArray;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * A PostCommandsTransformer implementation tailored for Gson JsonArrays.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class RawPostCommandsTransformer implements PostCommandsTransformer {
  private final JsonArray commands;

  /**
   * Creates a new raw post commands transformer instance.
   *
   * @param commands A list of your Discord bot's application commands in the form of Discord API's
   *     raw JSON format.
   * @since 1.0.0
   */
  public RawPostCommandsTransformer(final JsonArray commands) {
    this.commands = commands;
  }

  /**
   * Retrieves a list of your Discord bot's application commands in the form of Discord API's raw
   * JSON format.
   *
   * @return CompletableFuture&lt;String&gt; A list of your Discord bot's application commands in
   *     the form of Discord API's raw JSON format.
   * @since 1.0.0
   */
  @Override
  public CompletionStage<String> toJsonString() {
    return CompletableFuture.completedFuture(commands.toString());
  }
}
