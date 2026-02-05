package org.discordbots.api.client.webhooks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class EclipseJetty<T> extends HttpServlet {
    private final Class<T> aClass;
    private final byte[] authorization;
    private final Gson gson;

    public EclipseJetty(final Class<T> aClass, final String authorization) {
        this.aClass = aClass;
        this.authorization = authorization.getBytes(StandardCharsets.UTF_8);
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            final String signatureHeader = request.getHeader("x-topgg-signature");

            assert signatureHeader != null;

            final HashMap<String, String> parsedSignature = Arrays.stream(signatureHeader.split(",")).map(part -> part.split("=", 2)).collect(Collectors.toMap(
                part -> part[0].trim(),
                part -> part[1].trim(),
                (existing, replacement) -> replacement,
                HashMap::new
            ));

            final String signature = parsedSignature.get("v1");
            final String timestamp = parsedSignature.get("t");

            assert signature != null && timestamp != null;

            final SecretKeySpec key = new SecretKeySpec(this.authorization, "HmacSHA256");
            final Mac hmac = Mac.getInstance("HmacSHA256");

            hmac.init(key);

            final String body = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            final byte[] digest = hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

            if (!signature.equals(HexFormat.of().formatHex(digest))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Authorization");

                return;
            }

            callback(gson.fromJson(body, aClass));

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.getWriter().write("");
        } catch (final NoSuchAlgorithmException | InvalidKeyException | ArrayIndexOutOfBoundsException | AssertionError | JsonSyntaxException | JsonIOException | IOException error) {
            if (error instanceof NoSuchAlgorithmException || error instanceof InvalidKeyException) {
                throw new ServletException("Unable to find HMAC SHA-256 algorithm", error);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid Request");
            }
        }
    }

    public abstract void callback(T data);
}