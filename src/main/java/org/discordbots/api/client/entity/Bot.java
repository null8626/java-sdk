package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;
import java.util.List;

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

    @SerializedName("longdesc")
    private String longDescription;
    @SerializedName("shortdesc")
    private String shortDescription;

    @SerializedName("date") // rename so that the naming actually makes sense
    private OffsetDateTime submissionTime;

    @SerializedName("server_count")
    private Long serverCount;

    private int monthlyPoints;
    private int points;

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
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

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public OffsetDateTime getSubmissionTime() {
        return submissionTime;
    }

    public Long getServerCount() {
        return serverCount;
    }

    public int getMonthlyPoints() {
        return monthlyPoints;
    }

    public int getPoints() {
        return points;
    }

}
