# Top.gg Java SDK

The community-maintained Java SDK for Top.gg.

## Chapters

- [Installation](#installation)
- [Capabilities](#capabilities)
- [Setting up](#setting-up)
- [Usage](#usage)
  - [Getting your project's information](#getting-your-projects-information)
  - [Getting your project's vote information of a user](#getting-your-projects-vote-information-of-a-user)
  - [Getting a cursor-based paginated list of votes for your project](#getting-a-cursor-based-paginated-list-of-votes-for-your-project)
  - [Posting your bot's application commands list](#posting-your-bots-application-commands-list)
  - [Generating widget URLs](#generating-widget-urls)
  - [Webhooks](#webhooks)

## Installation

### Gradle

Add the following line to the `dependencies` section of your `build.gradle`:

```groovy
implementation 'gg.top:SDK:1.0.0'
```

### Maven

Add the following line to the `dependencies` section of your `pom.xml`:

```xml
<dependency>
  <groupId>gg.top</groupId>
  <artifactId>SDK</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Capabilities

This library provides several capabilities that can be enabled/disabled, such as:

- **`jdaWrapper`**: Additional wrappers for working with [JDA](https://github.com/discord-jda/JDA).
- **`discord4jWrapper`**: Additional wrappers for working with [Discord4J](https://github.com/Discord4J/Discord4J).
- **`webhooks`**: Accessing deserializable webhook payload classes.
  - **`dropwizardWebhooks`**: Wrapper for working with the [Dropwizard](https://www.dropwizard.io/en/stable/) web framework.
  - **`eclipseJettyWebhooks`**: Wrapper for working with the [Eclipse Jetty](https://jetty.org/index.html) web framework.
  - **`springBootWebhooks`**: Wrapper for working with the [Spring Boot](https://spring.io/projects/spring-boot/) web framework.

## Setting up

```java
import gg.top.api.TopggAPI;

final TopggAPI client = new TopggAPI(System.getenv("TOPGG_TOKEN"));
```

## Usage

### Getting your project's information

```java
client.getSelf().whenComplete((project, error) -> {
  if (error != null) {
    System.err.println("Error: " + error.getMessage());
  } else {
    // ...
  }
});
```

### Getting your project's vote information of a user

#### Discord ID

```java
import gg.top.api.entity.UserSource;

client.getVote(UserSource.DISCORD, "661200758510977084").whenComplete((vote, error) -> {
  if (error != null) {
    System.err.println("Error: " + error.getMessage());
  } else {
    // ...
  }
});
```

#### Top.gg ID

```java
import gg.top.api.entity.UserSource;

client.getVote(UserSource.TOPGG, "8226924471638491136").whenComplete((vote, error) -> {
  if (error != null) {
    System.err.println("Error: " + error.getMessage());
  } else {
    // ...
  }
});
```

### Getting a cursor-based paginated list of votes for your project

```java
import gg.top.api.entity.Vote;

final OffsetDateTime since = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

client.getVotes(since).whenComplete((firstPage, error) -> {
  if (error != null) {
    System.err.println("Error: " + error.getMessage());
  } else {
    final List<Vote> firstPageVotes = firstPage.getVotes();

    firstPage.next().whenComplete((secondPage, secondError) => {
      if (secondError != null) {
        System.err.println("Error: " + secondError.getMessage());
      } else {
        final List<Vote> secondPageVotes = secondPage.getVotes();

        // ...
      }
    });
  }
});
```

### Posting your bot's application commands list

#### JDA

> **NOTE**: This requires the `jdaWrapper` capability.

```java
final JDA jda = ...;

client.postCommands(jda);
```

#### Discord4J

> **NOTE**: This requires the `discord4jWrapper` capability.

```java
final DiscordClient bot = ...;

client.postCommands(bot);
```

#### Raw

```java
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

// Array of application commands that
// can be serialized to Discord API's raw JSON format.
final String commandsJson =
  "[{" +
  "  \"id\": \"1\"," +
  "  \"type\": 1," +
  "  \"application_id\": \"1\"," +
  "  \"name\": \"test\"," +
  "  \"description\": \"command description\"," +
  "  \"default_member_permissions\": \"\"," +
  "  \"version\": \"1\"" +
  "}]";

final JsonArray commands = JsonParser.parseString(commandsJson).getAsJsonArray();

client.postCommands(commands);
```

### Generating widget URLs

#### Large

```java
import gg.top.api.TopggWidget;
import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

final String widgetUrl = TopggWidget.large(Platform.DISCORD, ProjectType.BOT, "1026525568344264724");
```

#### Votes

```java
import gg.top.api.TopggWidget;
import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

final String widgetUrl = TopggWidget.votes(Platform.DISCORD, ProjectType.BOT, "1026525568344264724");
```

#### Owner

```java
import gg.top.api.TopggWidget;
import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

final String widgetUrl = TopggWidget.owner(Platform.DISCORD, ProjectType.BOT, "1026525568344264724");
```

#### Social

```java
import gg.top.api.TopggWidget;
import gg.top.api.entity.Platform;
import gg.top.api.entity.ProjectType;

final String widgetUrl = TopggWidget.social(Platform.DISCORD, ProjectType.BOT, "1026525568344264724");
```

### Webhooks

#### Dropwizard

> **NOTE**: This requires the `dropwizardWebhooks` capability.

In your `Webhooks.java`:

```java
import gg.top.webhooks.dropwizard.TopggWebhooks;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

// POST /webhook
@Path("/webhook")
public class Webhooks extends TopggWebhooks {
  public Webhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  // Optional
  @Override
  public Response onIntegrationCreate(final IntegrationCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  // Optional
  @Override
  public Response onIntegrationDelete(final IntegrationDeletePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  // Optional
  @Override
  public Response onTest(final TestPayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  // Optional
  @Override
  public Response onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
```

Later, in your server's `run` function:

```java
env.jersey().register(new Webhooks());
```

#### Eclipse Jetty

> **NOTE**: This requires the `eclipseJettyWebhooks` capability.

In your `Webhooks.java`:

```java
import gg.top.webhooks.eclipsejetty.TopggWebhooks;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

import jakarta.servlet.http.HttpServletResponse;

public class Webhooks extends TopggWebhooks {
  public Webhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  // Optional
  @Override
  public void onIntegrationCreate(
      final HttpServletResponse response,
      final IntegrationCreatePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  // Optional
  @Override
  public void onIntegrationDelete(
      final HttpServletResponse response,
      final IntegrationDeletePayload payload,
      final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  // Optional
  @Override
  public void onTest(
      final HttpServletResponse response, final TestPayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  // Optional
  @Override
  public void onVoteCreate(
      final HttpServletResponse response, final VoteCreatePayload payload, final String trace) {
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
```

Later, in your server's setup:

```java
// POST /webhook
context.addServlet(new ServletHolder(new Webhooks()), "/webhook");
```

#### Spring Boot

> **NOTE**: This requires the `springBootWebhooks` capability.

In your `Webhooks.java`:

```java
import gg.top.webhooks.springboot.TopggWebhooks;
import gg.top.webhooks.payload.IntegrationCreatePayload;
import gg.top.webhooks.payload.IntegrationDeletePayload;
import gg.top.webhooks.payload.TestPayload;
import gg.top.webhooks.payload.VoteCreatePayload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Webhooks extends TopggWebhooks<String> {
  public Webhooks() {
    super(System.getenv("TOPGG_WEBHOOK_SECRET"));
  }

  // POST /webhook
  @PostMapping("/webhook")
  public ResponseEntity<String> main(
      @RequestBody final String body,
      @RequestHeader("x-topgg-signature") final String signature,
      @RequestHeader("x-topgg-trace") final String trace) {
    return dispatch(body, signature, trace);
  }

  // Optional
  @Override
  public ResponseEntity<String> onIntegrationCreate(
      final IntegrationCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // Optional
  @Override
  public ResponseEntity<String> onIntegrationDelete(
      final IntegrationDeletePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // Optional
  @Override
  public ResponseEntity<String> onTest(final TestPayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // Optional
  @Override
  public ResponseEntity<String> onVoteCreate(final VoteCreatePayload payload, final String trace) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
```
