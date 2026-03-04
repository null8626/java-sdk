package org.discordbots.api;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.discordbots.api.entity.PaginatedVotes;
import org.discordbots.api.entity.PartialVote;
import org.discordbots.api.entity.Project;
import org.discordbots.api.entity.UserSource;
import org.discordbots.api.io.DefaultResponseTransformer;
import org.discordbots.api.io.EmptyResponseTransformer;
import org.discordbots.api.io.PaginatedVotesConverter;
import org.discordbots.api.io.PostCommandsTransformer;
import org.discordbots.api.io.RawPostCommandsTransformer;
import org.discordbots.api.io.ResponseTransformer;
import org.discordbots.api.io.UnsuccessfulHttpException;

public class DBLAPI {
  private static final HttpUrl baseUrl =
      new HttpUrl.Builder()
          .scheme("https")
          .host("top.gg")
          .addPathSegment("api")
          .addPathSegment("v1")
          .build();

  private final OkHttpClient httpClient;
  private final Gson gson;

  public DBLAPI(final OkHttpClient httpClient) {
    this.gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .registerTypeAdapter(PaginatedVotes.class, new PaginatedVotesConverter(this))
            .create();

    this.httpClient = httpClient;
  }

  public DBLAPI(final String token) {
    this(
        new OkHttpClient.Builder()
            .addInterceptor(
                (chain) ->
                    chain.proceed(
                        chain
                            .request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build()))
            .build());
  }

  public CompletionStage<Project> getSelf() {
    final HttpUrl url =
        baseUrl.newBuilder().addPathSegment("projects").addPathSegment("@me").build();

    return get(url, Project.class);
  }

  public CompletionStage<Void> postCommands(final PostCommandsTransformer commands) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("commands")
            .build();

    return commands
        .toJsonString()
        .thenCompose(jsonBody -> post(url, jsonBody, new EmptyResponseTransformer()));
  }

  public CompletionStage<Void> postCommands(final JsonArray commands) {
    return postCommands(new RawPostCommandsTransformer(commands));
  }

  public CompletionStage<PartialVote> getVote(final UserSource userSource, final String id) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("votes")
            .addPathSegment(id)
            .addQueryParameter("source", gson.toJson(userSource))
            .build();

    return get(url, PartialVote.class)
        .exceptionally(
            error -> {
              if (error instanceof UnsuccessfulHttpException
                  && ((UnsuccessfulHttpException) error).getResponse().code() == 404) {
                return null;
              }

              throw new CompletionException(error);
            });
  }

  public CompletionStage<PaginatedVotes> getVotes(final TemporalAccessor since) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("votes")
            .addQueryParameter("startDate", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(since))
            .build();

    return get(url, PaginatedVotes.class);
  }

  public CompletionStage<PaginatedVotes> getVotes(final String cursor) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("votes")
            .addQueryParameter("cursor", cursor)
            .build();

    return get(url, PaginatedVotes.class);
  }

  private <E> CompletionStage<E> get(final HttpUrl url, final Class<E> aClass) {
    return get(url, new DefaultResponseTransformer<>(aClass, gson));
  }

  private <E> CompletionStage<E> get(
      final HttpUrl url, final ResponseTransformer<E> responseTransformer) {
    return execute(new Request.Builder().get().url(url).build(), responseTransformer);
  }

  private <E> CompletionStage<E> post(
      final HttpUrl url, final String jsonBody, final ResponseTransformer<E> responseTransformer) {
    final RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
    final Request req = new Request.Builder().post(body).url(url).build();

    return execute(req, responseTransformer);
  }

  private <E> CompletionStage<E> execute(
      final Request request, final ResponseTransformer<E> responseTransformer) {
    final Call call = httpClient.newCall(request);
    final CompletableFuture<E> future = new CompletableFuture<>();

    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(Call call, IOException error) {
            future.completeExceptionally(error);
          }

          @Override
          @SuppressWarnings("UseSpecificCatch")
          public void onResponse(Call call, Response response) {
            try {
              if (response.isSuccessful()) {
                future.complete(responseTransformer.transform(response));
              } else {
                future.completeExceptionally(new UnsuccessfulHttpException(response));
              }
            } catch (final Throwable error) {
              future.completeExceptionally(error);
            } finally {
              response.body().close();
            }
          }
        });

    return future;
  }
}
