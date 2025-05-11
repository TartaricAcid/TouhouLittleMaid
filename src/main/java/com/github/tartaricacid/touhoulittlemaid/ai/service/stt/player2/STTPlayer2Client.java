package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.response.Message;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.response.STTPlayer2Callback;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatMessage;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

public class STTPlayer2Client implements STTClient {
    private static final String PLAYER2_GAME_KEY = "player2-game-key";
    private final HttpClient httpClient;
    private String baseUrl = "";

    private STTPlayer2Client(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static STTPlayer2Client create(final HttpClient httpClient) {
        return new STTPlayer2Client(httpClient);
    }

    private static String getStartUrl() {
        return "/start";
    }

    private static String getStopUrl() {
        return "/stop";
    }

    public STTPlayer2Client baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    @Override
    public void startRecord(Player player, EntityMaid maid) {
        this.start(message -> {
        }, throwable -> {
            String cause = throwable.getLocalizedMessage();
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.connect.fail")
                    .append(cause).withStyle(ChatFormatting.RED));
        });
    }

    @Override
    public void stopRecord(Player player, EntityMaid maid) {
        this.stop(message -> {
            String chatText = message.getText();
            if (StringUtils.isNotBlank(chatText)) {
                LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
                LanguageInfo info = languageManager.getLanguage(languageManager.getSelected());
                String language;
                if (info != null) {
                    language = info.toComponent().getString();
                } else {
                    language = "English (US)";
                }
                NetworkHandler.CHANNEL.sendToServer(new SendUserChatMessage(maid.getId(), chatText, language));
                String name = player.getScoreboardName();
                String format = String.format("<%s> %s", name, chatText);
                player.sendSystemMessage(Component.literal(format).withStyle(ChatFormatting.GRAY));
            } else {
                player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.content.empty").withStyle(ChatFormatting.GRAY));
            }
        }, throwable -> {
            String cause = throwable.getLocalizedMessage();
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.connect.fail")
                    .append(cause).withStyle(ChatFormatting.RED));
        });
    }

    private void start(Consumer<Message> consumer, Consumer<Throwable> failConsumer) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(PLAYER2_GAME_KEY, "TouhouLittleMaid")
                .POST(HttpRequest.BodyPublishers.ofString("{\"timeout\":30}"))
                .timeout(Duration.ofSeconds(20))
                .uri(URI.create(baseUrl + STTPlayer2Client.getStartUrl()));
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) -> {
                    STTPlayer2Callback callback = new STTPlayer2Callback(consumer);
                    if (throwable != null) {
                        callback.onFailure(httpRequest, throwable);
                        failConsumer.accept(throwable);
                    } else {
                        callback.onResponse(response, failConsumer);
                    }
                });
    }

    private void stop(Consumer<Message> consumer, Consumer<Throwable> failConsumer) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header(PLAYER2_GAME_KEY, "TouhouLittleMaid")
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(20))
                .uri(URI.create(baseUrl + STTPlayer2Client.getStopUrl()));
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) -> {
                    STTPlayer2Callback callback = new STTPlayer2Callback(consumer);
                    if (throwable != null) {
                        callback.onFailure(httpRequest, throwable);
                        failConsumer.accept(throwable);
                    } else {
                        callback.onResponse(response, failConsumer);
                    }
                });
    }
}
