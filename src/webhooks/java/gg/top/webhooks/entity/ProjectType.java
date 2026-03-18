package gg.top.webhooks.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A project's type.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ProjectType {
  /**
   * The project is a bot.
   *
   * @since 1.0.0
   */
  @SerializedName("bot")
  BOT,

  /**
   * The project is a server.
   *
   * @since 1.0.0
   */
  @SerializedName("server")
  SERVER
}
