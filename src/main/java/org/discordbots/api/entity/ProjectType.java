package org.discordbots.api.entity;

import com.google.gson.annotations.SerializedName;

public enum ProjectType {
  @SerializedName("bot")
  DISCORD_BOT,

  @SerializedName("server")
  DISCORD_SERVER;

  public String asWidgetPath() {
    return switch (this) {
      case ProjectType.DISCORD_BOT -> "discord/bot";
      case ProjectType.DISCORD_SERVER -> "discord/server";
    };
  }
}
