package gg.top.api.io;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.Response;

/**
 * A generic JSON response transformer.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultResponseTransformer<E> implements ResponseTransformer<E> {
  private final Class<E> aClass;
  private final Gson gson;

  /**
   * Creates a new generic JSON response transformer instance.
   *
   * @param aClass The corresponding class to deserialize to.
   * @param gson The Gson instance to use.
   * @since 1.0.0
   */
  public DefaultResponseTransformer(final Class<E> aClass, final Gson gson) {
    this.aClass = aClass;
    this.gson = gson;
  }

  /**
   * Tries to transform an HTTP response body to a deserialized object.
   *
   * @param response The HTTP response with a parsable body.
   * @return E The deserialized object.
   * @throws IOException Unable to parse HTTP response body.
   * @throws JsonSyntaxException Unable to deserialize the HTTP response body to the specified class.
   * @since 1.0.0
   */
  @Override
  public E transform(final Response response) throws IOException {
    return gson.fromJson(response.body().string(), aClass);
  }
}
