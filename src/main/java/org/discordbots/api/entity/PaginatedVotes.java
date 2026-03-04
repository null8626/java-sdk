package org.discordbots.api.entity;

import java.util.List;
import java.util.concurrent.CompletionStage;
import org.discordbots.api.DiscordBotListAPI;

public class PaginatedVotes {
  private final List<Vote> votes;
  private final String cursor;
  private final DiscordBotListAPI client;

  public PaginatedVotes(
      final List<Vote> votes, final String cursor, final DiscordBotListAPI client) {
    this.votes = votes;
    this.cursor = cursor;
    this.client = client;
  }

  public List<Vote> getVotes() {
    return votes;
  }

  public CompletionStage<PaginatedVotes> next() {
    return client.getVotes(cursor);
  }
}
