package org.discordbots.api.jda;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.concurrent.CompletionStage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.discordbots.api.client.io.PostCommandsTransformer;

public class JDAPostCommandsTransformer implements PostCommandsTransformer {
  private final JDA jda;

  public JDAPostCommandsTransformer(final JDA jda) {
    this.jda = jda;
  }

  @Override
  public CompletionStage<String> toJsonString() {
    return this.jda
        .retrieveCommands()
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
