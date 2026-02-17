package org.discordbots.api.client.webhooks;

import com.google.gson.annotations.SerializedName;

public class PartialProject {
  private String id;

  private ProjectType type;

  private Platform platform;

  @SerializedName("platform_id")
  private String platformId;

  public String getId() {
    return id;
  }

  public ProjectType getType() {
    return type;
  }

  public Platform getPlatform() {
    return platform;
  }

  public String getPlatformId() {
    return platformId;
  }
}