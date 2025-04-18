package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class BotStats {

    @SerializedName("server_count")
    private int serverCount;

    public int getServerCount() { return serverCount; }
}
