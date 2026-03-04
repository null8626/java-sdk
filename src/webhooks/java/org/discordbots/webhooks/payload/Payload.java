package org.discordbots.webhooks.payload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Payload {
  private String type;

  private JsonObject data;

  public String getType() {
    return type;
  }

  public <T> T getData(Gson gson, Class<T> cls) throws JsonSyntaxException {
    return gson.fromJson(data, cls);
  }
}
