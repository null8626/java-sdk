package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

public class User {
  private String id;
  private String name;

  @SerializedName("avatar_url")
  private String avatar;

  @SerializedName("platform_id")
  private String platformId;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getPlatformId() {
    return platformId;
  }
}
