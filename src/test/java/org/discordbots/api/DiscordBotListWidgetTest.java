package org.discordbots.api;

import org.discordbots.api.entity.ProjectType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class DiscordBotListWidgetTest {
  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void large(final ProjectType projectType) {
    DiscordBotListWidget.large(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void votes(final ProjectType projectType) {
    DiscordBotListWidget.votes(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void owner(final ProjectType projectType) {
    DiscordBotListWidget.owner(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void social(final ProjectType projectType) {
    DiscordBotListWidget.social(projectType, "123456");
  }
}
