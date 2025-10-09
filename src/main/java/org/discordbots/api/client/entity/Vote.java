package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

public class Vote {

    @SerializedName("bot")
    private String botId;

    @SerializedName("guild")
    private String serverId;

    @SerializedName("user")
    private String voterId;

    private String type;

    private String query;

    @SerializedName("isWeekend")
    private boolean weekend;

    public String getReceiverId() {
        return botId == null ? serverId : botId;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }

    public boolean isWeekend() {
        return weekend;
    }

}
