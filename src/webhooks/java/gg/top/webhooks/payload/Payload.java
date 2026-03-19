package gg.top.webhooks.payload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * A Top.gg webhook payload.
 *
 * @see IntegrationCreatePayload
 * @see IntegrationDeletePayload
 * @see TestPayload
 * @see VoteCreatePayload
 * @author null8626 &amp; Top.gg
 * @version 1.0.0
 * @since 1.0.0
 */
public class Payload {
  private String type;

  private JsonObject data;

  /**
   * The payload's type.
   *
   * @return String
   * @since 1.0.0
   */
  public String getType() {
    return type;
  }

  /**
   * Tries to deserialize the payload's data.
   *
   * @param <T> The target JSON deserializable class.
   * @param gson The Gson instance to use.
   * @param cls The target JSON deserializable class.
   * @return T The deserialized object.
   * @throws JsonSyntaxException Unable to deserialize the payload's data to the specified class.
   * @since 1.0.0
   */
  public <T> T getData(final Gson gson, final Class<T> cls) throws JsonSyntaxException {
    return gson.fromJson(data, cls);
  }
}
