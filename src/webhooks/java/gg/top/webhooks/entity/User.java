package gg.top.webhooks.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A Top.gg user.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class User {
  private String id;

  private String name;

  @SerializedName("avatar_url")
  private String avatar;

  @SerializedName("platform_id")
  private String platformId;

  /**
   * The user's ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getId() {
    return id;
  }

  /**
   * The user's name.
   *
   * @return String
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * The user's avatar URL.
   *
   * @return String
   * @since 1.0.0
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * The user's platform ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getPlatformId() {
    return platformId;
  }
}
