package gg.top.webhooks.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A brief information on a project listed on Top.gg.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class PartialProject {
  private String id;

  private ProjectType type;

  private Platform platform;

  @SerializedName("platform_id")
  private String platformId;

  /**
   * The project's ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getId() {
    return id;
  }

  /**
   * The project's type.
   *
   * @return ProjectType
   * @since 1.0.0
   */
  public ProjectType getType() {
    return type;
  }

  /**
   * The project's platform.
   *
   * @return Platform
   * @since 1.0.0
   */
  public Platform getPlatform() {
    return platform;
  }

  /**
   * The project's platform ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getPlatformId() {
    return platformId;
  }
}
