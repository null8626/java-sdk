package org.discordbots.api.client.project;

public final class Widget {
    private final static String BASE_URL = "https://top.gg/api/v1";

    public static enum Type {
        DISCORD_BOT,
        DISCORD_SERVER,
    }
  
    public static String large(Type ty, String id) {
        return BASE_URL + "/widgets/large/" + ty.name().toLowerCase().replace('_', '/') + "/" + id;
    }

    public static String votes(Type ty, String id) {
        return BASE_URL + "/widgets/small/votes/" + ty.name().toLowerCase().replace('_', '/') + "/" + id;
    }

    public static String owner(Type ty, String id) {
        return BASE_URL + "/widgets/small/owner/" + ty.name().toLowerCase().replace('_', '/') + "/" + id;
    }

    public static String social(Type ty, String id) {
        return BASE_URL + "/widgets/small/social/" + ty.name().toLowerCase().replace('_', '/') + "/" + id;
    }
}