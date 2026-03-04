package org.discordbots.api.entity;

import com.google.gson.annotations.SerializedName;
import java.time.OffsetDateTime;

public class PartialVote {
  @SerializedName("created_at")
  private OffsetDateTime votedAt;

  @SerializedName("expires_at")
  private OffsetDateTime expiresAt;

  private int weight;

  public OffsetDateTime getVotedAt() {
    return votedAt;
  }

  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public int getWeight() {
    return weight;
  }
}
