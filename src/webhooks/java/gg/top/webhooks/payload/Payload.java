package gg.top.webhooks.payload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Payload {
  private String type;

  private JsonObject data;

  public String getType() {
    return type;
  }

  public <T> T getData(final Gson gson, final Class<T> cls) throws JsonSyntaxException {
    return gson.fromJson(data, cls);
  }
}
