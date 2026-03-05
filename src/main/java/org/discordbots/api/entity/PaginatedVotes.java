package org.discordbots.api.entity;

import java.util.List;
import java.util.concurrent.CompletionStage;
import org.discordbots.api.DBLAPI;

public class PaginatedVotes {
  private final List<Vote> votes;
  private final String cursor;
  private final DBLAPI client;

  public PaginatedVotes(final List<Vote> votes, final String cursor, final DBLAPI client) {
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
