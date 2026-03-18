package gg.top.api;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import gg.top.api.entity.PaginatedVotes;
import gg.top.api.entity.PartialVote;
import gg.top.api.entity.Project;
import gg.top.api.entity.UserSource;
import gg.top.api.io.DefaultResponseTransformer;
import gg.top.api.io.EmptyResponseTransformer;
import gg.top.api.io.PaginatedVotesConverter;
import gg.top.api.io.PostCommandsTransformer;
import gg.top.api.io.RawPostCommandsTransformer;
import gg.top.api.io.ResponseTransformer;
import gg.top.api.io.UnsuccessfulHttpException;
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

/**
 * Interact with Top.gg API v1's endpoints.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class TopggAPI {
  private static final HttpUrl BASE_URL =
      new HttpUrl.Builder()
          .scheme("https")
          .host("top.gg")
          .addPathSegment("api")
          .addPathSegment("v1")
          .build();

  private final OkHttpClient httpClient;
  private final Gson gson;

  /**
   * Creates a new client instance.
   *
   * @param httpClient The existing HTTP client to use.
   * @since 1.0.0
   */
  public TopggAPI(final OkHttpClient httpClient) {
    gson =
        new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
            .registerTypeAdapter(PaginatedVotes.class, new PaginatedVotesConverter(this))
            .create();

    this.httpClient = httpClient;
  }

  /**
   * Creates a new client instance.
   *
   * @param token The API token to use.
   * @since 1.0.0
   */
  public TopggAPI(final String token) {
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

  /**
   * Tries to get your project's information.
   *
   * @return CompletableFuture&lt;Project&gt; Your project's information.
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<Project> getSelf() {
    final HttpUrl url =
        BASE_URL.newBuilder().addPathSegment("projects").addPathSegment("@me").build();

    return get(url, Project.class);
  }

  /**
   * Tries to update the application commands list in your Discord bot's Top.gg page.
   *
   * @param commands A list of your Discord bot's application commands in the form of an object that
   *     implements PostCommandsTransformer.
   * @return CompletableFuture&lt;Void&gt;
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<Void> postCommands(final PostCommandsTransformer commands) {
    final HttpUrl url =
        BASE_URL
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("commands")
            .build();

    return commands
        .toJsonString()
        .thenCompose(jsonBody -> post(url, jsonBody, new EmptyResponseTransformer()));
  }

  /**
   * Tries to update the application commands list in your Discord bot's Top.gg page.
   *
   * @param commands A list of your Discord bot's application commands in the form of Discord API's
   *     raw JSON format.
   * @return CompletableFuture&lt;Void&gt;
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<Void> postCommands(final JsonArray commands) {
    return postCommands(new RawPostCommandsTransformer(commands));
  }

  /**
   * Tries to get the latest vote information of a user on your project. Returns null if the user
   * has not voted.
   *
   * @param userSource The user's source.
   * @param id The user's ID.
   * @return CompletableFuture&lt;PartialVote&gt; The latest vote information of a user on your
   *     project or null if the user has not voted.
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<PartialVote> getVote(final UserSource userSource, final String id) {
    final HttpUrl url =
        BASE_URL
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

  /**
   * Tries to get a cursor-based paginated list of votes for your project, ordered by creation date.
   *
   * @param since The earliest possible date for all votes.
   * @return CompletableFuture&lt;PaginatedVotes&gt; A cursor-based paginated list of votes for your
   *     project, ordered by creation date.
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<PaginatedVotes> getVotes(final TemporalAccessor since) {
    final HttpUrl url =
        BASE_URL
            .newBuilder()
            .addPathSegment("projects")
            .addPathSegment("@me")
            .addPathSegment("votes")
            .addQueryParameter("startDate", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(since))
            .build();

    return get(url, PaginatedVotes.class);
  }

  /**
   * Tries to get a cursor-based paginated list of votes for your project, ordered by creation date.
   *
   * @param cursor The reference page cursor to use.
   * @return CompletableFuture&lt;PaginatedVotes&gt; A cursor-based paginated list of votes for your
   *     project, ordered by creation date.
   * @throws UnsuccessfulHttpException The client has received a non-favorable response from the
   *     API.
   * @since 1.0.0
   */
  public CompletionStage<PaginatedVotes> getVotes(final String cursor) {
    final HttpUrl url =
        BASE_URL
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
          public void onFailure(final Call call, final IOException error) {
            future.completeExceptionally(error);
          }

          @Override
          @SuppressWarnings("UseSpecificCatch")
          public void onResponse(final Call call, final Response response) {
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
