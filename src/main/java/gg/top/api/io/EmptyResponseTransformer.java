package gg.top.api.io;

import okhttp3.Response;

/**
 * A void type transformer.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmptyResponseTransformer implements ResponseTransformer<Void> {
  /**
   * Transforms an HTTP response to a null/void instance.
   *
   * @param response The HTTP response to transform. Unused.
   * @return A null/void instance.
   * @since 1.0.0
   */
  @Override
  public Void transform(final Response response) {
    return null;
  }
}
