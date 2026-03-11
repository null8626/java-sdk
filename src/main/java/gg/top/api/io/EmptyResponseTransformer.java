package gg.top.api.io;

import okhttp3.Response;

public class EmptyResponseTransformer implements ResponseTransformer<Void> {
  @Override
  public Void transform(final Response response) {
    return null;
  }
}
