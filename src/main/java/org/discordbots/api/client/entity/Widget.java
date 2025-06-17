package org.discordbots.api.client.entity;

public final class Widget {
    private final static String BASE_URL = "https://top.gg/api/v1";
  
    public static String large(String id) {
        return BASE_URL + "/widgets/large/" + id;
    }
}