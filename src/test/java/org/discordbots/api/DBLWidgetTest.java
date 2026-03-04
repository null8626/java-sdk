package org.discordbots.api;

import org.discordbots.api.entity.ProjectType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class DBLWidgetTest {
  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void large(final ProjectType projectType) {
    DBLWidget.large(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void votes(final ProjectType projectType) {
    DBLWidget.votes(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void owner(final ProjectType projectType) {
    DBLWidget.owner(projectType, "123456");
  }

  @ParameterizedTest
  @EnumSource(ProjectType.class)
  public void social(final ProjectType projectType) {
    DBLWidget.social(projectType, "123456");
  }
}
