package org.discordbots.api.client.io;

import java.util.concurrent.CompletionStage;

public interface PostCommandsTransformer {
  CompletionStage<String> toJsonString();
}
