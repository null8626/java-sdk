package org.discordbots.api.client.webhooks;

import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class EclipseJetty<T> extends HttpServlet {
    private final Class<T> aClass;
    private final String authorization;
    private final Gson gson;

    public EclipseJetty(final Class<T> aClass, final String authorization) {
        this.aClass = aClass;
        this.authorization = authorization;
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader == null || !authorizationHeader.equals(this.authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");

            return;
        }

        try {
            callback(gson.fromJson(new InputStreamReader(request.getInputStream()), aClass));

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.getWriter().write("");
            
            return;
        } catch (final JsonSyntaxException | JsonIOException | IOException ignored) {}

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Bad request");
    }

    public abstract void callback(T data);
}