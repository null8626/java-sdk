package gg.top.api.entity;

import gg.top.api.TopggAPI;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * A paginated list of a project's vote information.
 *
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class PaginatedVotes {
  private final List<Vote> votes;
  private final String cursor;
  private final TopggAPI client;

  /**
   * Creates a new paginated votes instance.
   *
   * @param votes The votes in this page.
   * @param cursor The reference page cursor to use.
   * @param client The client reference to use.
   * @since 1.0.0
   */
  public PaginatedVotes(final List<Vote> votes, final String cursor, final TopggAPI client) {
    this.votes = votes;
    this.cursor = cursor;
    this.client = client;
  }

  /**
   * Gets the votes in this page.
   *
   * @return List&lt;Vote&gt; The votes in this page.
   * @since 1.0.0
   */
  public List<Vote> getVotes() {
    return votes;
  }

  /**
   * Tries to advance to the next page.
   *
   * @return CompletionStage&lt;PaginatedVotes&gt; The next page of votes.
   * @since 1.0.0
   */
  public CompletionStage<PaginatedVotes> next() {
    return client.getVotes(cursor);
  }
}
