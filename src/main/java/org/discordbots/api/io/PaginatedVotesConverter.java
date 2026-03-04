package org.discordbots.api.io;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.discordbots.api.DBLAPI;
import org.discordbots.api.entity.PaginatedVotes;
import org.discordbots.api.entity.Vote;

public class PaginatedVotesConverter implements JsonDeserializer<PaginatedVotes> {
  private final DBLAPI client;

  public PaginatedVotesConverter(final DBLAPI client) {
    this.client = client;
  }

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
