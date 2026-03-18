package gg.top.webhooks.payload;

import com.google.gson.annotations.SerializedName;

/**
 * An 'integration.delete' webhook payload. Fires when a user has disconnected from your webhook
 * integration.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class IntegrationDeletePayload {
  @SerializedName("connection_id")
  private String connectionId;

  /**
   * The unique identifier for this connection.
   *
   * @return String
   * @since 1.0.0
   */
  public String getConnectionId() {
    return connectionId;
  }
}
