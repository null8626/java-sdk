package gg.top.api.io;

import com.google.gson.JsonArray;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class RawPostCommandsTransformer implements PostCommandsTransformer {
  private final JsonArray object;

  public RawPostCommandsTransformer(final JsonArray object) {
    this.object = object;
  }

  @Override
  public CompletionStage<String> toJsonString() {
    return CompletableFuture.completedFuture(object.toString());
  }
}
