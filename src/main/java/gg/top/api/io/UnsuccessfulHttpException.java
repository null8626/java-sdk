package gg.top.api.io;

import okhttp3.Response;

/**
 * Thrown upon HTTP request failure. Extends Exception.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class UnsuccessfulHttpException extends Exception {
  private final Response response;

  /**
   * Creates a new unsuccessful HTTP exception instance.
   *
   * @param response The HTTP response that caused this exception.
   * @since 1.0.0
   */
  public UnsuccessfulHttpException(final Response response) {
    super(
        "The server responded with code: " + response.code() + ", message: " + response.message());

    this.response = response;
  }

  /**
   * Retrieves the HTTP response that caused this exception.
   *
   * @return Response The HTTP response that caused this exception.
   * @since 1.0.0
   */
  public Response getResponse() {
    return response;
  }
}
