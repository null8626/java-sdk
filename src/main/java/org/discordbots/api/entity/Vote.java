package org.discordbots.api.entity;

import com.google.gson.annotations.SerializedName;
import java.time.OffsetDateTime;

public class Vote {
  @SerializedName("user_id")
  private String userId;

  @SerializedName("platform_id")
  private String platformId;

  @SerializedName("created_at")
  private OffsetDateTime votedAt;

  @SerializedName("expires_at")
  private OffsetDateTime expiresAt;

  private int weight;

  public String getUserId() {
    return userId;
  }

  public String getPlatformId() {
    return platformId;
  }

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
