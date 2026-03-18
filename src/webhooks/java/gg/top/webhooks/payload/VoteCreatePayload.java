package gg.top.webhooks.payload;

import com.google.gson.annotations.SerializedName;
import gg.top.webhooks.entity.PartialProject;
import gg.top.webhooks.entity.User;
import java.time.OffsetDateTime;

/**
 * A 'vote.create' webhook payload. Fires when a user votes for your project.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class VoteCreatePayload {
  private String id;

  private int weight;

  @SerializedName("created_at")
  private OffsetDateTime votedAt;

  @SerializedName("expires_at")
  private OffsetDateTime expiresAt;

  private PartialProject project;

  private User user;

  /**
   * The vote's ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getId() {
    return id;
  }

  /**
   * The number of votes this vote counted for. This is a rounded integer value which determines how
   * many points this individual vote was worth.
   *
   * @return int
   * @since 1.0.0
   */
  public int getWeight() {
    return weight;
  }

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
   * The project that received this vote.
   *
   * @return PartialProject
   * @since 1.0.0
   */
  public PartialProject getProject() {
    return project;
  }

  /**
   * The user who voted for this project.
   *
   * @return User
   * @since 1.0.0
   */
  public User getUser() {
    return user;
  }
}
