package gg.top.api.io;

import java.util.concurrent.CompletionStage;

public interface PostCommandsTransformer {
  CompletionStage<String> toJsonString();
}
