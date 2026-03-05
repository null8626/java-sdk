package org.discordbots.api;

import org.discordbots.api.entity.ProjectType;

public final class DBLWidget {
  private static final String BASE_URL = "https://top.gg/api/v1/widgets";

  public static String large(final ProjectType projectType, final String id) {
    return BASE_URL + "/large/" + projectType.asWidgetPath() + "/" + id;
  }

  public static String votes(final ProjectType projectType, final String id) {
    return BASE_URL + "/small/votes/" + projectType.asWidgetPath() + "/" + id;
  }

  public static String owner(final ProjectType projectType, final String id) {
    return BASE_URL + "/small/owner/" + projectType.asWidgetPath() + "/" + id;
  }

  public static String social(final ProjectType projectType, final String id) {
    return BASE_URL + "/small/social/" + projectType.asWidgetPath() + "/" + id;
  }
}
