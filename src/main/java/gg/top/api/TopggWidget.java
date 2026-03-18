package gg.top.api;

import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

/**
 * A Top.gg widget URL generator.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public final class TopggWidget {
  private static final String BASE_URL = "https://top.gg/api/v1/widgets";

  /**
   * Generates a large widget URL.
   *
   * @param platform The project's platform.
   * @param projectType The project's type.
   * @param id The project's ID.
   * @return String The widget URL.
   * @since 1.0.0
   */
  public static String large(
      final Platform platform, final ProjectType projectType, final String id) {
    return String.format(
        "%s/large/%s/%s/%s",
        BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  /**
   * Generates a small widget URL for displaying votes.
   *
   * @param platform The project's platform.
   * @param projectType The project's type.
   * @param id The project's ID.
   * @return String The widget URL.
   * @since 1.0.0
   */
  public static String votes(
      final Platform platform, final ProjectType projectType, final String id) {
    return String.format(
        "%s/small/votes/%s/%s/%s",
        BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  /**
   * Generates a small widget URL for displaying a project's owner.
   *
   * @param platform The project's platform.
   * @param projectType The project's type.
   * @param id The project's ID.
   * @return String The widget URL.
   * @since 1.0.0
   */
  public static String owner(
      final Platform platform, final ProjectType projectType, final String id) {
    return String.format(
        "%s/small/owner/%s/%s/%s",
        BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  /**
   * Generates a small widget URL for displaying social stats.
   *
   * @param platform The project's platform.
   * @param projectType The project's type.
   * @param id The project's ID.
   * @return String The widget URL.
   * @since 1.0.0
   */
  public static String social(
      final Platform platform, final ProjectType projectType, final String id) {
    return String.format(
        "%s/small/social/%s/%s/%s",
        BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }
}
