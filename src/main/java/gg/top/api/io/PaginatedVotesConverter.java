package gg.top.api.io;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.top.api.TopggAPI;
import gg.top.api.entity.PaginatedVotes;
import gg.top.api.entity.Vote;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A transformer tailored for deserializing into PaginatedVotes.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class PaginatedVotesConverter implements JsonDeserializer<PaginatedVotes> {
  private final TopggAPI client;

  /**
   * Creates a new paginated votes converter instance.
   *
   * @param client The client reference to use.
   * @since 1.0.0
   */
  public PaginatedVotesConverter(final TopggAPI client) {
    this.client = client;
  }

  /**
   * Deserializes a JSON object to PaginatedVotes.
   *
   * @param json The corresponding JSON object to deserialize from.
   * @param typeOfT The type of T. Unused.
   * @param context The JSON deserialization context to use.
   * @throws IllegalStateException The specified JSON is an invalid type.
   * @since 1.0.0
   */
  @Override
  public PaginatedVotes deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    final JsonObject object = json.getAsJsonObject();

    final List<Vote> votes =
        StreamSupport.stream(object.getAsJsonArray("data").spliterator(), false)
            .map(vote -> (Vote) context.deserialize(vote, Vote.class))
            .collect(Collectors.toList());
    final String cursor = object.get("cursor").getAsString();

    return new PaginatedVotes(votes, cursor, client);
  }
}
