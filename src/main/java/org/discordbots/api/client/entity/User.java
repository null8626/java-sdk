package org.discordbots.api.client.entity;

import com.google.gson.annotations.SerializedName;

public class User extends SimpleUser {


    private boolean admin, mod, webMod;
    private boolean supporter;

    private Social social;



    public String getDefaultAvatar() {
        return defaultAvatar;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isMod() {
        return mod;
    }

    public boolean isWebMod() {
        return webMod;
    }

    public boolean isSupporter() {
        return supporter;
    }

    public Social getSocial() {
        return social;
    }

}
