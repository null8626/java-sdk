package org.discordbots.api.client.io;

import okhttp3.Response;

public class UnsuccessfulHttpException extends Exception {
  private final Response response;

  public UnsuccessfulHttpException(Response response) {
    super(
        "The server responded with code: " + response.code() + ", message: " + response.message());

    this.response = response;
  }

  public Response getResponse() {
    return response;
  }
}
