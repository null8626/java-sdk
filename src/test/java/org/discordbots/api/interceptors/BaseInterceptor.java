package org.discordbots.api.interceptors;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class BaseInterceptor implements Interceptor {
  @SuppressWarnings("FieldMayBeFinal")
  private String response;

  public BaseInterceptor() {
    try {
      final String className = getClass().getSimpleName();

      final InputStream inputStream =
          BaseInterceptor.class.getResourceAsStream(
              "/" + className.substring(0, className.length() - 11) + "Response.json");

      this.response = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException | NullPointerException ignored) {
      this.response = "";
    }
  }

  protected abstract boolean isCorrect(final String method, final String path, final HttpUrl url);

  protected abstract int getStatusCode();

  protected abstract String getMessage();

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();

    final HttpUrl url = request.url();
    final String path = String.join("/", url.pathSegments());

    if (url.host().equals("top.gg")
        && path.startsWith("api/v1")
        && isCorrect(request.method(), path, url)) {
      return new Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(getStatusCode())
          .message(getMessage())
          .body(ResponseBody.create(response, MediaType.get("application/json")))
          .addHeader("content-type", "application/json")
          .build();
    }

    return chain.proceed(request);
  }
}
