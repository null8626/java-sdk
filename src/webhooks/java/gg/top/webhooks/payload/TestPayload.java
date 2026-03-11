package gg.top.webhooks.payload;

import gg.top.webhooks.entity.PartialProject;
import gg.top.webhooks.entity.User;

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
