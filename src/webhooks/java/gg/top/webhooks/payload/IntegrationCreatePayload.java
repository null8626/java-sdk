package gg.top.webhooks.payload;

import com.google.gson.annotations.SerializedName;

import gg.top.webhooks.entity.PartialProject;
import gg.top.webhooks.entity.User;

public class IntegrationCreatePayload {
  @SerializedName("connection_id")
  private String connectionId;

  @SerializedName("webhook_secret")
  private String secret;

  private PartialProject project;

  private User user;

  public String getConnectionId() {
    return connectionId;
  }

  public String getSecret() {
    return secret;
  }

  public PartialProject getProject() {
    return project;
  }

  public User getUser() {
    return user;
  }
}
