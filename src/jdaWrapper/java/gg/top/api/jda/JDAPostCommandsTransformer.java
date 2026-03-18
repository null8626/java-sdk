package gg.top.api.jda;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import gg.top.api.io.PostCommandsTransformer;
import java.util.concurrent.CompletionStage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A PostCommandsTransformer implementation tailored for JDA bots.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class JDAPostCommandsTransformer implements PostCommandsTransformer {
  private final JDA jda;

  /**
   * Creates a new JDA post commands transformer instance.
   *
   * @param client The JDA reference to use.
   * @since 1.0.0
   */
  public JDAPostCommandsTransformer(final JDA jda) {
    this.jda = jda;
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
    return jda.retrieveCommands()
        .submit()
        .thenApply(
            commands -> {
              final JsonArray object = new JsonArray();

              for (final Command command : commands) {
                object.add(
                    JsonParser.parseString(CommandData.fromCommand(command).toData().toString()));
              }

              return object.toString();
            });
  }
}
