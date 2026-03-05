package org.discordbots.webhooks.entity;

import com.google.gson.annotations.SerializedName;

public enum ProjectType {
  @SerializedName("bot")
  DISCORD_BOT,

  @SerializedName("server")
  DISCORD_SERVER
}
