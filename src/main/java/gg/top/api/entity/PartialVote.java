package gg.top.api.entity;

import java.time.OffsetDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * A brief information of a project's vote.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class PartialVote {
  @SerializedName("created_at")
  private OffsetDateTime votedAt;

  @SerializedName("expires_at")
  private OffsetDateTime expiresAt;

  private int weight;

  /**
   * When the vote was cast.
   *
   * @return OffsetDateTime
   * @since 1.0.0
   */
  public OffsetDateTime getVotedAt() {
    return votedAt;
  }

  /**
   * When the vote expires and the user is required to vote again.
   *
   * @return OffsetDateTime
   * @since 1.0.0
   */
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  /**
   * The number of votes this vote counted for. This is a rounded integer value which determines how many points this individual vote was worth.
   *
   * @return int
   * @since 1.0.0
   */
  public int getWeight() {
    return weight;
  }
}
