package org.discordbots.api.client.project;

import com.google.gson.annotations.SerializedName;

public class VotingMultiplier {

    @SerializedName("is_weekend")
    private boolean weekend;

    public boolean isWeekend() {
        return weekend;
    }

}
