package gg.top.api;

import gg.top.api.entity.ProjectType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TopggWidgetTests {
  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void large(final ProjectType projectType) {
    TopggWidget.large(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void votes(final ProjectType projectType) {
    TopggWidget.votes(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void owner(final ProjectType projectType) {
    TopggWidget.owner(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void social(final ProjectType projectType) {
    TopggWidget.social(projectType, "123456");
  }
}
