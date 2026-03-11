package gg.top.api;

import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

public final class TopggWidget {
  private static final String BASE_URL = "https://top.gg/api/v1/widgets";

  public static String large(final Platform platform, final ProjectType projectType, final String id) {
    return String.format("%s/large/%s/%s/%s", BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  public static String votes(final Platform platform, final ProjectType projectType, final String id) {
    return String.format("%s/small/votes/%s/%s/%s", BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  public static String owner(final Platform platform, final ProjectType projectType, final String id) {
    return String.format("%s/small/owner/%s/%s/%s", BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }

  public static String social(final Platform platform, final ProjectType projectType, final String id) {
    return String.format("%s/small/social/%s/%s/%s", BASE_URL, platform.name().toLowerCase(), projectType.name().toLowerCase(), id);
  }
}
