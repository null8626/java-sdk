package gg.top.api.interceptors;

import okhttp3.HttpUrl;

public class PostCommandsInterceptor extends BaseInterceptor {
  @Override
  protected boolean isCorrect(final String method, final String path, final HttpUrl url) {
    return method.equals("POST") && path.endsWith("/projects/@me/commands");
  }

  @Override
  protected int getStatusCode() {
    return 204;
  }

  @Override
  protected String getMessage() {
    return "No Content";
  }
}
