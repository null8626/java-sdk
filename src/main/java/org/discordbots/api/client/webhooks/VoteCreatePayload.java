package org.discordbots.api.client.webhooks;

import java.time.OffsetDateTime;

import com.google.gson.annotations.SerializedName;

public class VoteCreatePayload {
  private String id;

  private int weight;

  @SerializedName("voted_at")
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