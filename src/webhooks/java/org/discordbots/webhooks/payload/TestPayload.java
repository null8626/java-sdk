package org.discordbots.webhooks.payload;

import org.discordbots.webhooks.entity.PartialProject;
import org.discordbots.webhooks.entity.User;

public class TestPayload {
  private PartialProject project;

  private User user;

  public PartialProject getProject() {
    return project;
  }

  public User getUser() {
    return user;
  }
}
