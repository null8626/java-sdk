package gg.top.api.interceptors;

import okhttp3.HttpUrl;

public class GetSelfInterceptor extends BaseInterceptor {
  @Override
  protected boolean isCorrect(final String method, final String path, final HttpUrl url) {
    return method.equals("GET") && path.endsWith("/projects/@me");
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
