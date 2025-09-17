package org.discordbots.api.client.project;

import com.google.gson.annotations.SerializedName;

public class BotStats {

    @SerializedName("server_count")
    private Long serverCount;

    public Long getBotServerCount() { return serverCount; }
}
