package org.discordbots.api.client.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.discordbots.api.client.DiscordBotListAPI;
import org.discordbots.api.client.entity.Bot;
import org.discordbots.api.client.entity.BotResult;
import org.discordbots.api.client.entity.BotStats;
import org.discordbots.api.client.entity.SimpleUser;
import org.discordbots.api.client.entity.VotingMultiplier;
import org.discordbots.api.client.io.DefaultResponseTransformer;
import org.discordbots.api.client.io.EmptyResponseTransformer;
import org.discordbots.api.client.io.ResponseTransformer;
import org.discordbots.api.client.io.UnsuccessfulHttpException;
import org.json.JSONObject;

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DiscordBotListAPIImpl implements DiscordBotListAPI {

    private static final HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("top.gg")
            .addPathSegment("api")
            .addPathSegment("v1")
            .build();

    private final OkHttpClient httpClient;
    private final Gson gson;

    private final String token, botId;

    private final ScheduledExecutorService autoposterScheduler;
    private ScheduledFuture<?> autoposterFuture;
    private final AtomicBoolean isAutoposterCancelled;

    public DiscordBotListAPIImpl(String token, String botId) {
        this.token = token;
        this.botId = botId;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter())
                .create();

        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor((chain) -> {
                    Request req = chain.request().newBuilder()
                            .addHeader("Authorization", this.token)
                            .build();
                    return chain.proceed(req);
                })
                .build();
        
        this.autoposterScheduler = Executors.newSingleThreadScheduledExecutor();
        this.autoposterFuture = null;
        this.isAutoposterCancelled = new AtomicBoolean(false);
    }

    @Override
    public void startAutoposter(int delayInSeconds, Supplier<Integer> statsCallback, BiConsumer<Integer, ? super Throwable> postCallback) {
        if (this.autoposterFuture != null && !this.autoposterFuture.isCancelled()) {
            return;
        }

        if (delayInSeconds < 900) {
            delayInSeconds = 900;
        }

        this.autoposterFuture = this.autoposterScheduler.scheduleAtFixedRate(() -> {
            if (!this.isAutoposterCancelled.get()) {
                final int serverCount = statsCallback.get();
                CompletionStage<Void> response = this.setStats(serverCount);

                if (postCallback != null) {
                    response.whenComplete((_, error) -> {
                        if (error == null) {
                            postCallback.accept(serverCount, null);
                        } else {
                            postCallback.accept(null, error);
                            this.stopAutoposter();
                        }
                    });
                }
            }
        }, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void startAutoposter(int delayInSeconds, Supplier<Integer> statsCallback) {
        this.startAutoposter(delayInSeconds, statsCallback, null);
    }

    @Override
    public void startAutoposter(Supplier<Integer> statsCallback, BiConsumer<Integer, ? super Throwable> postCallback) {
        this.startAutoposter(900, statsCallback, postCallback);
    }

    @Override
    public void startAutoposter(Supplier<Integer> statsCallback) {
        this.startAutoposter(900, statsCallback, null);
    }

    @Override
    public void stopAutoposter() {
        this.isAutoposterCancelled.set(true);
        
        if (this.autoposterFuture != null) {
            this.autoposterFuture.cancel(false);
        }

        this.autoposterScheduler.shutdownNow();
    }

    @Override
    public CompletionStage<Void> setStats(int serverCount) throws IllegalArgumentException {
        if (serverCount <= 0) {
            throw new IllegalArgumentException("The provided server count cannot be less than 1!");
        }

        JSONObject json = new JSONObject()
                .put("server_count", serverCount);

        return setStats(json);
    }

    private CompletionStage<Void> setStats(JSONObject jsonBody) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addPathSegment("stats")
                .build();

        return post(url, jsonBody, new EmptyResponseTransformer());
    }

    @Override
    public CompletionStage<BotStats> getStats() {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addPathSegment("stats")
                .build();

        return get(url, BotStats.class);
    }

    @Override
    public CompletionStage<List<SimpleUser>> getVoters() {
        return getVoters(0);
    }
    
    @Override
    public CompletionStage<List<SimpleUser>> getVoters(int page) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addPathSegment(botId)
                .addPathSegment("votes")
                .addQueryParameter("page", String.valueOf(page <= 0 ? 1 : page))
                .build();

        return get(url, resp -> {
            // This is kinda awkward but this is done so that it can return it was a list instead of
            // an array
            ResponseTransformer<SimpleUser[]> arrayTransformer = new DefaultResponseTransformer<>(SimpleUser[].class, gson);
            return Arrays.asList(arrayTransformer.transform(resp));
        });
    }

    @Override
    public CompletionStage<Bot> getBot(String botId) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addPathSegment(botId)
                .build();

        return get(url, Bot.class);
    }

    @Override
    public CompletionStage<BotResult> getBots() {
        return getBots(50, 0, null);
    }
    
    @Override
    public CompletionStage<BotResult> getBots(int limit) {
        return getBots(limit, 0, null);
    }
    
    @Override
    public CompletionStage<BotResult> getBots(int limit, int offset) {
        return getBots(limit, offset, null);
    }

    @Override
    public CompletionStage<BotResult> getBots(int limit, int offset, String sort) {
        return getBots(limit, offset, sort, null);
    }


    @Override
    public CompletionStage<BotResult> getBots(int limit, int offset, String sort, List<String> fields) {
        if (limit > 500) {
            limit = 500;
        } else if (limit <= 0) {
            limit = 50;
        }

        if (offset <= 0) {
            offset = 0;
        }

        HttpUrl.Builder urlBuilder = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("offset", String.valueOf(offset));

        if (sort != null && (sort.equals("monthlyPoints") || sort.equals("id") || sort.equals("date"))) {
            urlBuilder.addQueryParameter("sort", sort);
        }

        if (fields != null) {
            String fieldsString = fields.stream()
                    .collect(Collectors.joining(" "));

            urlBuilder.addQueryParameter("fields", fieldsString);
        }

        return get(urlBuilder.build(), BotResult.class);
    }

    @Override
    public CompletionStage<Boolean> hasVoted(String userId) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("bots")
                .addPathSegment("check")
                .addQueryParameter("userId", userId)
                .build();

        return get(url, (resp) -> {
            final ResponseBody body = resp.body();

            if (body == null) {
                return false;
            }

            final JSONObject json = new JSONObject(body.string());

            return json.getInt("voted") == 1;
        });
    }

    @Override
    public CompletionStage<VotingMultiplier> getVotingMultiplier() {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("weekend")
                .build();

        return get(url, VotingMultiplier.class);
    }

    private <E> CompletionStage<E> get(HttpUrl url, Class<E> aClass) {
        return get(url, new DefaultResponseTransformer<>(aClass, gson));
    }

    private <E> CompletionStage<E> get(HttpUrl url, ResponseTransformer<E> responseTransformer) {
        Request req = new Request.Builder()
                .get()
                .url(url)
                .build();

        return execute(req, responseTransformer);
    }

    // The class provided in this is kinda unneeded because the only thing ever given to it
    // is Void, but I wanted to make it expandable (maybe some post methods will return objects
    // in the future)
    // private <E> CompletionStage<E> post(HttpUrl url, JSONObject jsonBody, Class<E> aClass) {
    //     return post(url, jsonBody, new DefaultResponseTransformer<>(aClass, gson));
    // }

    private <E> CompletionStage<E> post(HttpUrl url, JSONObject jsonBody, ResponseTransformer<E> responseTransformer) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonBody.toString(), mediaType);

        Request req = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        return execute(req, responseTransformer);
    }

    private <E> CompletionStage<E> execute(Request request, ResponseTransformer<E> responseTransformer) {
        Call call = httpClient.newCall(request);

        final CompletableFuture<E> future = new CompletableFuture<>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    if (response.isSuccessful()) {
                        E transformed = responseTransformer.transform(response);
                        future.complete(transformed);
                    } else {
                        String message = response.message();

                        // DBL sends error messages as part of the body and leaves the
                        // actual message blank so this will just pull that instead because
                        // it's 1000x more useful than the actual message
                        if (message.isEmpty()) {
                            try {
                                final ResponseBody body = response.body();

                                if (body != null) {
                                    message = (new JSONObject(body)).getString("error");
                                }
                            } catch (final Exception ignored) {}
                        }

                        Exception e = new UnsuccessfulHttpException(response.code(), message);
                        future.completeExceptionally(e);
                    }

                } catch (Exception e) {
                    future.completeExceptionally(e);
                } finally {
                    final ResponseBody body = response.body();

                    if (body != null) {
                        body.close();
                    }
                }
            }
        });

        return future;
    }

}