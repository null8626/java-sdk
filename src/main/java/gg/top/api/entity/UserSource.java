package gg.top.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A user account from an external platform that is linked to a Top.gg user account.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public enum UserSource {
  @SerializedName("discord")
  DISCORD,

  @SerializedName("topgg")
  TOPGG
}
