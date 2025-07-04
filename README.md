# Top.gg Java SDK

The community-maintained Java library for Top.gg.

## Installation

[![Release](https://jitpack.io/v/top-gg/java-sdk.svg)](https://jitpack.io/#top-gg/java-sdk)

Replace `VERSION` here with the latest version or commit hash. The latest version can be found [under GitHub releases](https://github.com/top-gg-community/java-sdk/releases).

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.top-gg</groupId>
    <artifactId>java-sdk</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle

```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  compile 'com.github.top-gg:java-sdk:VERSION'
}
```

## Setting up

```java
final DiscordBotListAPI client = new DiscordBotListAPI
  .Builder()
  .token(System.getEnv("TOPGG_TOKEN"))
  .botId("BOT_ID")
  .build();
```

## Usage

### Getting a bot

```java
final Bot bot = client.getBot("264811613708746752").toCompletableFuture().get();
```

### Getting several bots

#### With defaults

```java
final BotResult result = client.getBots().toCompletableFuture().get();
final List<Bot> bots = result.getResults();
```

#### With explicit arguments

```java
//                                      Limit  Offset  Sort by
final BotResult result = client.getBots(50,    0,      "date").toCompletableFuture().get();
final List<Bot> bots = result.getResults();
```

### Getting your bot's voters

#### First page

```java
final List<SimpleUser> voters = client.getVoters().toCompletableFuture().get();
```

#### Subsequent pages

```java
final List<SimpleUser> voters = client.getVoters(2).toCompletableFuture().get();
```

### Check if a user has voted for your bot

```java
final boolean hasVoted = client.hasVoted("661200758510977084").toCompletableFuture().get();
```

### Getting your bot's server count

```java
final long serverCount = client.getServerCount().toCompletableFuture().get();
```

### Posting your bot's server count

```java
client.postServerCount(bot.getServerCount()).toCompletableFuture().get();
```

### Automatically posting your bot's server count every few minutes

#### With defaults

```java
client.startAutoposter(() -> {
  return bot.getServerCount();
});
```

#### With a callback

```java
client.startAutoposter(() -> {
  return bot.getServerCount();
}, new BiConsumer<>() {
  @Override
  public void accept(Integer serverCount, Throwable error) {
    if (serverCount != null) {
      System.out.println("Successfully posted " + serverCount + " servers to Top.gg!");
    } else {
      System.err.println("Post error: " + error.getMessage());

      client.stopAutoposter();
    }
  }
});
```

#### With a custom delay

```java
// Posts once every 30 minutes
client.startAutoposter(1800, () -> {
  return bot.getServerCount();
}, new BiConsumer<>() {
  @Override
  public void accept(Integer serverCount, Throwable error) {
    if (serverCount != null) {
      System.out.println("Successfully posted " + serverCount + " servers to Top.gg!");
    } else {
      System.err.println("Post error: " + error.getMessage());

      client.stopAutoposter();
    }
  }
});
```

### Checking if the weekend vote multiplier is active

```java
final boolean multiplierActive = client.getVotingMultiplier().toCompletableFuture().get().isWeekend();
```

### Generating widget URLs

#### Large

```java
final String widgetUrl = Widget.large(Widget.Type.DISCORD_BOT, "574652751745777665");
```

#### Votes

```java
final String widgetUrl = Widget.votes(Widget.Type.DISCORD_BOT, "574652751745777665");
```

#### Owner

```java
final String widgetUrl = Widget.owner(Widget.Type.DISCORD_BOT, "574652751745777665");
```

#### Social

```java
final String widgetUrl = Widget.social(Widget.Type.DISCORD_BOT, "574652751745777665");
```

### Webhooks

#### Being notified whenever someone voted for your bot

##### Spring Boot

In your `TopggWebhookFilterConfig.java`:

```java
import org.discordbots.api.client.entity.Vote;
import org.discordbots.api.client.webhooks.SpringBoot;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopggWebhookFilterConfig {
    @Bean
    public FilterRegistrationBean<SpringBoot<Vote>> registerVoteWebhook() {
        final FilterRegistrationBean<SpringBoot<Vote>> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new SpringBoot<>(Vote.class, System.getenv("MY_TOPGG_WEBHOOK_SECRET")) {
            @Override
            public void callback(final Vote vote) {
                System.out.println("A user with the ID of " + vote.getVoterId() + " has voted us on Top.gg!");
            }
        });
        
        registrationBean.addUrlPatterns("/votes");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
```

##### Dropwizard

In your `MyVoteListener.java`:

```java
import org.discordbots.api.client.entity.Vote;
import org.discordbots.api.client.webhooks.Dropwizard;

import jakarta.ws.rs.Path;

@Path("/votes")
public class MyVoteListener extends Dropwizard<Vote> {
    public MyVoteListener() {
        super(Vote.class, System.getenv("MY_TOPGG_WEBHOOK_SECRET"));
    }

    @Override
    public void callback(final Vote vote) {
        System.out.println("A user with the ID of " + vote.getVoterId() + " has voted us on Top.gg!");
    }
}
```

In your `MyServer.java`:

```java
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jersey.setup.JerseyEnvironment;

public class MyServer extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new MyServer().run(args);
    }

    @Override
    public void run(Configuration config, Environment env) {
        final JerseyEnvironment jersey = env.jersey();
        
        jersey.register(new MyVoteListener());
    }
}
```

##### Eclipse Jetty

In your `MyServer.java`:

```java
import org.discordbots.api.client.entity.Vote;
import org.discordbots.api.client.webhooks.EclipseJetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MyServer {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(8080);
        final ServletContextHandler handler = new ServletContextHandler();

        handler.setContextPath("/");
        handler.addServlet(new ServletHolder(new EclipseJetty<>(Vote.class, System.getenv("MY_TOPGG_WEBHOOK_SECRET")) {
            @Override
            public void callback(final Vote vote) {
                System.out.println("A user with the ID of " + vote.getVoterId() + " has voted us on Top.gg!");
            }
        }), "/votes");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
```