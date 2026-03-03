package org.discordbots.api.client;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import okhttp3.*;
import org.discordbots.api.client.entity.PaginatedVotes;
import org.discordbots.api.client.entity.PartialVote;
import org.discordbots.api.client.entity.Project;
import org.discordbots.api.client.entity.UserSource;
import org.discordbots.api.client.io.DefaultResponseTransformer;
import org.discordbots.api.client.io.EmptyResponseTransformer;
import org.discordbots.api.client.io.PaginatedVotesConverter;
import org.discordbots.api.client.io.ResponseTransformer;
import org.discordbots.api.client.io.UnsuccessfulHttpException;

public class DiscordBotListAPI {
  private static final HttpUrl baseUrl =
      new HttpUrl.Builder()
          .scheme("https")
          .host("top.gg")
          .addPathSegment("api")
          .addPathSegment("v1")
          .build();

  private final OkHttpClient httpClient;
  private final Gson gson;

  private final String token;

  public DiscordBotListAPI(final String token) {
    this.token = "Bearer " + token;

    this.gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .registerTypeAdapter(PaginatedVotes.class, new PaginatedVotesConverter(this))
            .create();

    this.httpClient =
        new OkHttpClient.Builder()
            .addInterceptor(
                (chain) ->
                    chain.proceed(
                        chain
                            .request()
                            .newBuilder()
                            .addHeader("Authorization", this.token)
                            .build()))
            .build();
  }

  public CompletionStage<Project> getSelf() {
    final HttpUrl url =
        baseUrl.newBuilder().addPathSegment("projects").addPathSegment("@me").build();

    return get(url, Project.class);
  }

  public <C> CompletionStage<Void> postCommands(final JsonArray commands) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("commands")
            .build();

    return post(url, commands, new EmptyResponseTransformer());
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

  public CompletionStage<PaginatedVotes> getVotes(final OffsetDateTime since) {
    final HttpUrl url =
        baseUrl
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("votes")
            .addQueryParameter("startDate", since.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
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
      final HttpUrl url,
      final JsonArray jsonBody,
      final ResponseTransformer<E> responseTransformer) {
    final RequestBody body =
        RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));
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
          public void onResponse(Call call, Response response) {
            try {
              if (response.isSuccessful()) {
                future.complete(responseTransformer.transform(response));
              } else {
                future.completeExceptionally(new UnsuccessfulHttpException(response));
              }
            } catch (Throwable error) {
              future.completeExceptionally(error);
            } finally {
              response.body().close();
            }
          }
        });

    return future;
  }
}
