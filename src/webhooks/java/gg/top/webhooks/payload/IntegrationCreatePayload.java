package gg.top.webhooks.payload;

import com.google.gson.annotations.SerializedName;
import gg.top.webhooks.entity.PartialProject;
import gg.top.webhooks.entity.User;

/**
 * An 'integration.create' webhook payload. Fires when a user has connected to your webhook
 * integration.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class IntegrationCreatePayload {
  @SerializedName("connection_id")
  private String connectionId;

  @SerializedName("webhook_secret")
  private String secret;

  private PartialProject project;

  private User user;

  /**
   * The unique identifier for this connection.
   *
   * @return String
   * @since 1.0.0
   */
  public String getConnectionId() {
    return connectionId;
  }

  /**
   * The secret used to verify future webhook deliveries.
   *
   * @return String
   * @since 1.0.0
   */
  public String getSecret() {
    return secret;
  }

  /**
   * The project that the integration refers to.
   *
   * @return PartialProject
   * @since 1.0.0
   */
  public PartialProject getProject() {
    return project;
  }

  /**
   * The user who triggered this event.
   *
   * @return User
   * @since 1.0.0
   */
  public User getUser() {
    return user;
  }
}
