package org.discordbots.api.interceptors;

import okhttp3.HttpUrl;

public class GetVotesInterceptor extends BaseInterceptor {
  @Override
  protected boolean isCorrect(final String method, final String path, final HttpUrl url) {
    return method.equals("GET")
        && path.endsWith("/projects/@me/votes")
        && (url.queryParameter("startDate") != null || url.queryParameter("cursor") != null);
  }

  @Override
  protected int getStatusCode() {
    return 200;
  }

  @Override
  protected String getMessage() {
    return "OK";
  }
}
