package gg.top.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * A project's vote information.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class Vote extends PartialVote {
  @SerializedName("user_id")
  private String voterId;

  @SerializedName("platform_id")
  private String platformId;

  /**
   * The voter's ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getVoterId() {
    return voterId;
  }

  /**
   * The voter's ID on the project's platform.
   *
   * @return String
   * @since 1.0.0
   */
  public String getPlatformId() {
    return platformId;
  }
}
