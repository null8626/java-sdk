package gg.top.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A user account from an external platform that is linked to a Top.gg user account.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public enum UserSource {
  /**
   * The user is linked to Discord.
   *
   * @since 1.0.0
   */
  @SerializedName("discord")
  DISCORD,

  /**
   * The user is linked to Top.gg.
   *
   * @since 1.0.0
   */
  @SerializedName("topgg")
  TOPGG
}
