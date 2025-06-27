package org.discordbots.api.client.entity;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    public boolean isTest() {
        return type.equals("test");
    }

    public Map<String, String> getQuery() {
        Map<String, String> map = new HashMap<>();

        if (query != null) {
            if (query.startsWith("?")) {
                query = query.substring(1);
            }

            for (final String param : query.split("&")) {
                final String[] pair = param.split("=", 2);
                final String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                final String value = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
    
                map.put(key, value);
            }
        }

        return map;
    }

    public boolean isWeekend() {
        return weekend;
    }

}
