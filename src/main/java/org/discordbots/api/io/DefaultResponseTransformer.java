package org.discordbots.api.io;

import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Response;

public class DefaultResponseTransformer<E> implements ResponseTransformer<E> {
  private final Class<E> aClass;
  private final Gson gson;

  public DefaultResponseTransformer(final Class<E> aClass, final Gson gson) {
    this.aClass = aClass;
    this.gson = gson;
  }

  @Override
  public E transform(final Response response) throws IOException {
    return gson.fromJson(response.body().string(), aClass);
  }
}
