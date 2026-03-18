package gg.top.api.io;

import okhttp3.Response;

/**
 * A generic HTTP response transformer.
 *
 * @param <E> The target output class.
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ResponseTransformer<E> {
  /**
   * Tries to transform an HTTP response to the desired output.
   *
   * @param response The HTTP response to transform.
   * @return E The desired output.
   * @throws Exception An exception was thrown during the transformation process.
   * @since 1.0.0
   */
  E transform(final Response response) throws Exception;
}
