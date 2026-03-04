package org.discordbots.webhooks.payload;

import com.google.gson.annotations.SerializedName;
import java.time.OffsetDateTime;
import org.discordbots.webhooks.entity.PartialProject;
import org.discordbots.webhooks.entity.User;

public class VoteCreatePayload {
  private String id;

  private int weight;

  @SerializedName("created_at")
  private OffsetDateTime votedAt;

  @SerializedName("expires_at")
  private OffsetDateTime expiresAt;

  private PartialProject project;

  private User user;

  public String getId() {
    return id;
  }

  public int getWeight() {
    return weight;
  }

  public OffsetDateTime getVotedAt() {
    return votedAt;
  }

  public OffsetDateTime getExpiredAt() {
    return expiresAt;
  }

  public PartialProject getProject() {
    return project;
  }

  public User getUser() {
    return user;
  }
}
