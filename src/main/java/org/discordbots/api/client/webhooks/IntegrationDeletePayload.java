package org.discordbots.api.client.webhooks;

import com.google.gson.annotations.SerializedName;

public class IntegrationDeletePayload {
  @SerializedName("connection_id")
  private String connectionId;

  public String getConnectionId() {
    return connectionId;
  }
}
