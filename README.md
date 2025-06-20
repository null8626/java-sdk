# DBL Java Library
A Java wrapper for the [top.gg API](https://top.gg/api/docs)

## Usage

First, build a DiscordBotListAPI object.

```java
DiscordBotListAPI api = new DiscordBotListAPI.Builder()
    .token("token")
    .botId("botId")
    .build();
```

#### Posting stats

DBL provides three ways to post your bots stats.

**#1**
Posts the server count for the whole bot.
```java
int serverCount = ...; // the total amount of servers across all shards

api.setStats(serverCount);
```

#### Checking votes

```java
String userId = ...; // ID of the user you're checking
api.hasVoted(userId).whenComplete((hasVoted, e) -> {
    if(hasVoted)
        System.out.println("This person has voted!");
    else
        System.out.println("This person has not voted!");
});
```

#### Getting voting multiplier

```java
api.getVotingMultiplier().whenComplete((multiplier, e) -> {
    if(multiplier.isWeekend())
        System.out.println("It's the weekend, so votes are worth 2x!");
    else
        System.out.println("It's not the weekend :pensive:");
});
```

## Download

[![Release](https://jitpack.io/v/top-gg/java-sdk.svg)](https://jitpack.io/#top-gg/java-sdk)

Replace `VERSION` with the latest version or commit hash. The latest version can be found under releases.

#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.top-gg</groupId>
        <artifactId>java-sdk</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

#### Gradle 
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
```gradle
dependencies {
    compile 'com.github.top-gg:java-sdk:VERSION'
}
```


