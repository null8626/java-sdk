package gg.top.webhooks.springboot;

import java.io.IOException;
import gg.top.webhooks.Mocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TopggSpringBootWebhookTests {
  private static final String SECRET = System.getenv("TOPGG_WEBHOOK_SECRET");
  private static final String TRACE = "trace";
  private static Mocks MOCKS;

  @Autowired private MockMvc mvc;

  @BeforeAll
  public static void setup() throws IOException, NullPointerException {
    MOCKS = new Mocks();
  }

  private void send(final String name, final String payload) throws IOException, Exception {
    mvc.perform(
            MockMvcRequestBuilders.post("/webhook")
                .content(payload)
                .header("x-topgg-signature", Mocks.signature(SECRET, payload))
                .header("x-topgg-trace", TRACE)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andExpect(MockMvcResultMatchers.content().string("sb:" + name + "," + TRACE));
  }

  @Test
  public void integrationCreate() throws IOException, Exception {
    send("integrationCreate", MOCKS.integrationCreatePayload);
  }

  @Test
  public void integrationDelete() throws IOException, Exception {
    send("integrationDelete", MOCKS.integrationDeletePayload);
  }

  @Test
  public void test() throws IOException, Exception {
    send("test", MOCKS.testPayload);
  }

  @Test
  public void voteCreate() throws IOException, Exception {
    send("voteCreate", MOCKS.voteCreatePayload);
  }
}
