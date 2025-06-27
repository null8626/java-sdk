package org.discordbots.api.client.io;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class DefaultResponseTransformer<E> implements ResponseTransformer<E> {

    private final Class<E> aClass;
    private final Gson gson;

    public DefaultResponseTransformer(Class<E> aClass, Gson gson) {
        this.aClass = aClass;
        this.gson = gson;
    }

    @Override
    public E transform(Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
            if (responseBody != null) {
                return gson.fromJson(responseBody.charStream(), aClass);
            }
        }

        throw new IOException("Unable to parse JSON because of malformed response body.");
    }

}
