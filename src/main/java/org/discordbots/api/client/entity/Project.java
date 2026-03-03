package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Project {
  private String id;
  private String name;
  private Platform platform;
  private ProjectType type;
  private String headline;
  private List<String> tags;

  @SerializedName("votes")
  private long currentVotes;

  @SerializedName("votes_total")
  private long totalVotes;

  @SerializedName("review_score")
  private float reviewScore;

  @SerializedName("review_count")
  private long reviewCount;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Platform getPlatform() {
    return platform;
  }

  public ProjectType getType() {
    return type;
  }

  public String getHeadline() {
    return headline;
  }

  public List<String> getTags() {
    return tags;
  }

  public long getCurrentVotes() {
    return currentVotes;
  }

  public long getTotalVotes() {
    return totalVotes;
  }

  public float getReviewScore() {
    return reviewScore;
  }

  public long getReviewCount() {
    return reviewCount;
  }
}
