package org.discordbots.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import okhttp3.OkHttpClient;
import org.discordbots.api.entity.PaginatedVotes;
import org.discordbots.api.entity.UserSource;
import org.discordbots.api.interceptors.GetSelfInterceptor;
import org.discordbots.api.interceptors.GetVoteInterceptor;
import org.discordbots.api.interceptors.GetVotesInterceptor;
import org.discordbots.api.interceptors.PostCommandsInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class DBLAPITest {
  private DBLAPI client;

  @BeforeEach
  public void initiate() {
    this.client =
        new DBLAPI(
            new OkHttpClient.Builder()
                .addInterceptor(new GetSelfInterceptor())
                .addInterceptor(new GetVoteInterceptor())
                .addInterceptor(new GetVotesInterceptor())
                .addInterceptor(new PostCommandsInterceptor())
                .build());
  }

  @Test
  public void getSelf() {
    this.client.getSelf().toCompletableFuture().join();
  }

  @Test
  public void postCommands() {
    final JsonArray commands =
        JsonParser.parseReader(
                new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("PostCommands.json"),
                    StandardCharsets.UTF_8))
            .getAsJsonArray();

    this.client.postCommands(commands).toCompletableFuture().join();
  }

  @ParameterizedTest
  @EnumSource(UserSource.class)
  public void getVote(final UserSource userSource) {
    this.client.getVote(userSource, "123456").toCompletableFuture().join();
  }

  @Test
  @SuppressWarnings("unused")
  public void getVotes() {
    final PaginatedVotes firstPage =
        this.client
            .getVotes(OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
            .toCompletableFuture()
            .join();
    final PaginatedVotes secondPage = firstPage.next().toCompletableFuture().join();
  }
}
