package gg.top.webhooks.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A project's type.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ProjectType {
  @SerializedName("bot")
  BOT,

  @SerializedName("server")
  SERVER
}
