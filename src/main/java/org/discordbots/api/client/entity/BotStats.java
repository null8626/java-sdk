package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

public class BotStats {

    @SerializedName("server_count")
    private Long serverCount;

    public Long getServerCount() { return serverCount; }
}
