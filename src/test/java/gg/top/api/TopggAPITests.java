package gg.top.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import okhttp3.OkHttpClient;
import gg.top.api.entity.PaginatedVotes;
import gg.top.api.entity.UserSource;
import gg.top.api.interceptors.GetSelfInterceptor;
import gg.top.api.interceptors.GetVoteInterceptor;
import gg.top.api.interceptors.GetVotesInterceptor;
import gg.top.api.interceptors.PostCommandsInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TopggAPITests {
  private static TopggAPI CLIENT;

  @BeforeAll
  public static void setup() {
    CLIENT =
        new TopggAPI(
            new OkHttpClient.Builder()
                .addInterceptor(new GetSelfInterceptor())
                .addInterceptor(new GetVoteInterceptor())
                .addInterceptor(new GetVotesInterceptor())
                .addInterceptor(new PostCommandsInterceptor())
                .build());
  }

  @Test
  public void getSelf() {
    CLIENT.getSelf().toCompletableFuture().join();
  }

  @Test
  public void postCommands() {
    final JsonArray commands =
        JsonParser.parseReader(
                new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("PostCommands.json"),
                    StandardCharsets.UTF_8))
            .getAsJsonArray();

    CLIENT.postCommands(commands).toCompletableFuture().join();
  }

  @ParameterizedTest
  @EnumSource(UserSource.class)
  public void getVote(final UserSource userSource) {
    CLIENT.getVote(userSource, "123456").toCompletableFuture().join();
  }

  @Test
  @SuppressWarnings("unused")
  public void getVotes() {
    final PaginatedVotes firstPage =
        CLIENT
            .getVotes(OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
            .toCompletableFuture()
            .join();
    final PaginatedVotes secondPage = firstPage.next().toCompletableFuture().join();
  }
}
