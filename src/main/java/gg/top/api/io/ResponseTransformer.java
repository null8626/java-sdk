package gg.top.api.io;

import okhttp3.Response;

public interface ResponseTransformer<E> {
  E transform(final Response response) throws Exception;
}
