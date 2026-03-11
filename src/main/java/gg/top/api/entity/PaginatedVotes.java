package gg.top.api.entity;

import java.util.List;
import java.util.concurrent.CompletionStage;
import gg.top.api.TopggAPI;

public class PaginatedVotes {
  private final List<Vote> votes;
  private final String cursor;
  private final TopggAPI client;

  public PaginatedVotes(final List<Vote> votes, final String cursor, final TopggAPI client) {
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
