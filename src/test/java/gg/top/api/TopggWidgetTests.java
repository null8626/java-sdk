package gg.top.api;

import java.util.stream.Stream;

import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TopggWidgetTests {
  @SuppressWarnings("unused")
  static Stream<Arguments> platformsAndProjectTypes() {
    final Stream.Builder<Arguments> streamBuilder = Stream.builder();

    for (final Platform platform : Platform.values()) {
      for (final ProjectType projectType : ProjectType.values()) {
        streamBuilder.add(Arguments.of(platform, projectType));
      }
    }

    return streamBuilder.build();
  }

  @ParameterizedTest
  @MethodSource("platformsAndProjectTypes")
  public void large(final Platform platform, final ProjectType projectType) {
    TopggWidget.large(platform, projectType, "123456");
  }

  @ParameterizedTest
  @MethodSource("platformsAndProjectTypes")
  public void votes(final Platform platform, final ProjectType projectType) {
    TopggWidget.votes(platform, projectType, "123456");
  }

  @ParameterizedTest
  @MethodSource("platformsAndProjectTypes")
  public void owner(final Platform platform, final ProjectType projectType) {
    TopggWidget.owner(platform, projectType, "123456");
  }

  @ParameterizedTest
  @MethodSource("platformsAndProjectTypes")
  public void social(final Platform platform, final ProjectType projectType) {
    TopggWidget.social(platform, projectType, "123456");
  }
}
