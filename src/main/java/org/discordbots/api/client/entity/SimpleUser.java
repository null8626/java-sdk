package org.discordbots.api.client.entity;

public class SimpleUser {

    private String id;
    private String username;

    private String avatar;

    public String getId() {
        return id;
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

}
