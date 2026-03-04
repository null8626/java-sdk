package org.discordbots.api.io;

import okhttp3.Response;

public interface ResponseTransformer<E> {
  E transform(Response response) throws Exception;
}
