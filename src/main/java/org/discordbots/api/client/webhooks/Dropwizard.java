package org.discordbots.api.client.webhooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
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

public class Dropwizard {
  private final byte[] authorization;
  private final Gson gson;
  private final Dropwizard.Listener listener;

  public Dropwizard(final String authorization, final Dropwizard.Listener listener) {
    this.authorization = authorization.getBytes(StandardCharsets.UTF_8);
    this.gson = new GsonBuilder().create();
    this.listener = listener;
  }

  @POST
  public Response handle(@Context HttpServletRequest request) throws WebApplicationException {
    try {
      final String signatureHeader = request.getHeader("x-topgg-signature");

      assert signatureHeader != null;

      final HashMap<String, String> parsedSignature =
          Arrays.stream(signatureHeader.split(","))
              .map(part -> part.split("=", 2))
              .collect(
                  Collectors.toMap(
                      part -> part[0].trim(),
                      part -> part[1].trim(),
                      (existing, replacement) -> replacement,
                      HashMap::new));

      final String signature = parsedSignature.get("v1");
      final String timestamp = parsedSignature.get("t");

      assert signature != null && timestamp != null;

      final SecretKeySpec key = new SecretKeySpec(authorization, "HmacSHA256");
      final Mac hmac = Mac.getInstance("HmacSHA256");

      hmac.init(key);

      final String body =
          new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      final byte[] digest =
          hmac.doFinal(String.format("%s.%s", timestamp, body).getBytes(StandardCharsets.UTF_8));

      if (!signature.equals(HexFormat.of().formatHex(digest))) {
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity("Invalid Authorization")
            .build();
      }

      final Payload payload = gson.fromJson(body, Payload.class);
      final String trace = request.getHeader("x-topgg-trace");

      try {
        return switch (payload.getType()) {
          case "integration.create" ->
              listener.onIntegrationCreate(
                  payload.getData(gson, IntegrationCreatePayload.class), trace);
          case "integration.delete" ->
              listener.onIntegrationDelete(
                  payload.getData(gson, IntegrationDeletePayload.class), trace);
          case "webhook.test" -> listener.onTest(payload.getData(gson, TestPayload.class), trace);
          case "vote.create" ->
              listener.onVoteCreate(payload.getData(gson, VoteCreatePayload.class), trace);
          default -> Response.status(Response.Status.BAD_REQUEST).entity("Invalid Request").build();
        };
      } catch (Throwable ignored) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("Internal Server Error")
            .build();
      }
    } catch (final NoSuchAlgorithmException
        | InvalidKeyException
        | ArrayIndexOutOfBoundsException
        | AssertionError
        | JsonSyntaxException
        | JsonIOException
        | IOException error) {
      if (error instanceof NoSuchAlgorithmException || error instanceof InvalidKeyException) {
        throw new WebApplicationException("Unable to find HMAC SHA-256 algorithm", error);
      } else {
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Request").build();
      }
    }
  }

  public interface Listener {
    default Response onIntegrationCreate(IntegrationCreatePayload payload, String trace) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    default Response onIntegrationDelete(IntegrationDeletePayload payload, String trace) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    default Response onTest(TestPayload payload, String trace) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    default Response onVoteCreate(VoteCreatePayload payload, String trace) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }
  }
}
