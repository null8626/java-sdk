package gg.top.api.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * A project listed on Top.gg.
 *
 * @author null8626 & Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
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

  /**
   * The project's ID.
   *
   * @return String
   * @since 1.0.0
   */
  public String getId() {
    return id;
  }

  /**
   * The project's name sourced from the external platform.
   *
   * @return String
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * The project's platform.
   *
   * @return Platform
   * @since 1.0.0
   */
  public Platform getPlatform() {
    return platform;
  }

  /**
   * The project's type.
   *
   * @return ProjectType
   * @since 1.0.0
   */
  public ProjectType getType() {
    return type;
  }

  /**
   * The project's short description.
   *
   * @return String
   * @since 1.0.0
   */
  public String getHeadline() {
    return headline;
  }

  /**
   * The project's tag IDs.
   *
   * @return List&lt;String&gt;
   * @since 1.0.0
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * The project's current vote count that affects the project's ranking.
   *
   * @return long
   * @since 1.0.0
   */
  public long getCurrentVotes() {
    return currentVotes;
  }

  /**
   * The project's total vote count.
   *
   * @return long
   * @since 1.0.0
   */
  public long getTotalVotes() {
    return totalVotes;
  }

  /**
   * The project's review score out of 5.
   *
   * @return float
   * @since 1.0.0
   */
  public float getReviewScore() {
    return reviewScore;
  }

  /**
   * The project's total review count.
   *
   * @return long
   * @since 1.0.0
   */
  public long getReviewCount() {
    return reviewCount;
  }
}
