package org.discordbots.api.client.webhooks;

import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

public abstract class Dropwizard<T> {
    private final Class<T> aClass;
    private final String authorization;
    private final Gson gson;

    public Dropwizard(final Class<T> aClass, final String authorization) {
        this.aClass = aClass;
        this.authorization = authorization;
        this.gson = new GsonBuilder().create();
    }

    @POST
    public Response handle(@Context HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader == null || !authorizationHeader.equals(this.authorization)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        try {
            callback(gson.fromJson(new InputStreamReader(request.getInputStream()), aClass));

            return Response.noContent().build();
        } catch (final JsonSyntaxException | JsonIOException | IOException ignored) {}

        return Response.status(Response.Status.BAD_REQUEST).entity("Bad request").build();
    }

    public abstract void callback(T data);
}