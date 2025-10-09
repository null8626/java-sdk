package org.discordbots.api.client.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Bot {

    private String id;
    @SerializedName("clientid")
    private String clientId;
    private String username;

    private String avatar;

    private String prefix;
    private String invite;
    private String website;
    private String vanity;
    private String support;
    private List<String> tags;
    private List<String> owners;

    @SerializedName("longdesc")
    private String longDescription;
    @SerializedName("shortdesc")
    private String shortDescription;

    @SerializedName("github")
    private String githubRepository;

    @SerializedName("date") // rename so that the naming actually makes sense
    private OffsetDateTime submissionTime;

    @SerializedName("server_count")
    private Long serverCount;

    private int monthlyPoints;
    private int points;
    
    private Reviews reviews;

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return "0";
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDefaultAvatar() {
        return "";
    }

    public String getPrefix() {
        return prefix;
    }

    public String getInvite() {
        return invite;
    }

    public String getWebsite() {
        return website;
    }

    public String getVanity() {
        return vanity;
    }

    public String getSupport() {
        return support;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getOwners() {
        return owners;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getBetaDescription() {
        return "";
    }

    public boolean isCertified() {
        return false;
    }

    public String getGithubRepository() {
        return githubRepository;
    }

    public OffsetDateTime getSubmissionTime() {
        return submissionTime;
    }

    public Long getServerCount() {
        return serverCount;
    }

    public List<String> getGuilds() {
        return new ArrayList<>();
    }

    public List<Integer> getShards() {
        return new ArrayList<>();
    }

    public int getMonthlyPoints() {
        return monthlyPoints;
    }

    public int getPoints() {
        return points;
    }

    public boolean isLegacy() {
        return false;
    }

    public Reviews getReviews() {
        return reviews;
    }
}
