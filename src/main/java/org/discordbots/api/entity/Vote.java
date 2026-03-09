package org.discordbots.api.entity;

import com.google.gson.annotations.SerializedName;
import java.time.OffsetDateTime;

public class Vote extends PartialVote {
  @SerializedName("user_id")
  private String voterId;

  @SerializedName("platform_id")
  private String platformId;

  public String getVoterId() {
    return voterId;
  }

  public String getPlatformId() {
    return platformId;
  }
}
