package org.discordbots.webhooks.dropwizard;

import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;

public class CustomServer extends Application<Configuration> {
  public static void main(final String[] args) throws Exception {
    new CustomServer().run(args);
  }

  @Override
  public void run(final Configuration config, final Environment env) {
    env.jersey().register(new CustomWebhooks());
  }
}
