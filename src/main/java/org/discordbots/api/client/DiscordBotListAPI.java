package org.discordbots.api.client;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.discordbots.api.client.entity.Bot;
import org.discordbots.api.client.entity.BotResult;
import org.discordbots.api.client.entity.BotStats;
import org.discordbots.api.client.entity.SimpleUser;
import org.discordbots.api.client.entity.VotingMultiplier;
import org.discordbots.api.client.impl.DiscordBotListAPIImpl;

public interface DiscordBotListAPI {
    void startAutoposter(int delayInSeconds, Supplier<Integer> statsCallback, BiConsumer<Integer, ? super Throwable> postCallback);
    void startAutoposter(int delayInSeconds, Supplier<Integer> statsCallback);
    void startAutoposter(Supplier<Integer> statsCallback, BiConsumer<Integer, ? super Throwable> postCallback);
    void startAutoposter(Supplier<Integer> statsCallback);
    
    void stopAutoposter();

    CompletionStage<Void> setStats(int serverCount);

    CompletionStage<BotStats> getStats();

    CompletionStage<List<SimpleUser>> getVoters();
    CompletionStage<List<SimpleUser>> getVoters(int page);
    CompletionStage<Boolean> hasVoted(String userId);

    CompletionStage<BotResult> getBots();
    CompletionStage<BotResult> getBots(int limit);
    CompletionStage<BotResult> getBots(int limit, int offset);
    CompletionStage<BotResult> getBots(int limit, int offset, String sort);
    CompletionStage<BotResult> getBots(int limit, int offset, String sort, List<String> fields);
    
    CompletionStage<Bot> getBot(String botId);

    CompletionStage<VotingMultiplier> getVotingMultiplier();

    class Builder {

        // Required
        private String botId = null;
        private String token = null;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder botId(String botId) {
            this.botId = botId;
            return this;
        }

        public DiscordBotListAPI build() {
            if (token == null)
                throw new IllegalArgumentException("The provided token cannot be null!");

            if (botId == null)
                throw new IllegalArgumentException("The provided bot ID cannot be null!");

            return new DiscordBotListAPIImpl(token, botId);
        }

    }

}
