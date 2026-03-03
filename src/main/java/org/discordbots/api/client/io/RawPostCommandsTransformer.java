package org.discordbots.api.client.io;

import com.google.gson.JsonArray;

public class RawPostCommandsTransformer implements PostCommands {
  private final JsonArray object;

  public RawPostCommandsTransformer(final JsonArray object) {
    this.object = object;
  }

  @Override
  public String toJsonString() {
    return this.object.toString();
  }
}
