package gg.top.webhooks.payload;

import gg.top.webhooks.entity.PartialProject;
import gg.top.webhooks.entity.User;

/**
 * A 'webhook.test' webhook payload. Fires upon sent test from the project dashboard.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestPayload {
  private PartialProject project;

  private User user;

  /**
   * The project that the test refers to.
   *
   * @return PartialProject
   * @since 1.0.0
   */
  public PartialProject getProject() {
    return project;
  }

  /**
   * The user who triggered this test.
   *
   * @return User
   * @since 1.0.0
   */
  public User getUser() {
    return user;
  }
}
