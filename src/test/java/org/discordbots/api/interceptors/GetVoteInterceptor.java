package org.discordbots.api.interceptors;

import okhttp3.HttpUrl;

public class GetVoteInterceptor extends BaseInterceptor {
  @Override
  protected boolean isCorrect(final String method, final String path, final HttpUrl url) {
    return method.equals("GET")
        && path.contains("/projects/@me/votes/")
        && url.queryParameter("source") != null;
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
