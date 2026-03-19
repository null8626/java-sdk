package gg.top.api.io;

import java.util.concurrent.CompletionStage;

/**
 * A transformer that retrieves a list of your Discord bot's application commands in the form of
 * Discord API's raw JSON format.
 *
 * @see RawPostCommandsTransformer
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PostCommandsTransformer {
  /**
   * Retrieves a list of your Discord bot's application commands in the form of Discord API's raw
   * JSON format.
   *
   * @return CompletableFuture&lt;String&gt; A list of your Discord bot's application commands in
   *     the form of Discord API's raw JSON format.
   * @since 1.0.0
   */
  CompletionStage<String> toJsonString();
}
