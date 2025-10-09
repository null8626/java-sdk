package org.discordbots.api.client.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BotStats {

    @SerializedName("server_count")
    private Long serverCount;

    public Long getServerCount() { return serverCount; }
    public List<Integer> getShards() { return new ArrayList<>(); }
}
