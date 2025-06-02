package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TTSPlayer2Client implements TTSClient, TTSSystemServices {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClient httpClient;
    private final TTSPlayer2Site site;

    public TTSPlayer2Client(HttpClient httpClient, TTSPlayer2Site site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, @Nullable ResponseCallback<byte[]> callback) {
        if (isClient()) {
            handle(message, config);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void handle(String message, TTSConfig config) {
        URI url = URI.create(this.site.url());
        String model = config.model();

        TTSPlayer2Request request = TTSPlayer2Request.create()
                .setText(message).setVoiceId(model);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(request)))
                .timeout(MAX_TIMEOUT)
                .uri(url);

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();

        // 本地运行的时候，直接使用 APP 播放音频，故不会使用回调
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray()).whenComplete((response, throwable) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            if (throwable != null) {
                String cause = throwable.getLocalizedMessage();
                MutableComponent errorMessage = ErrorCode.getErrorMessage(ServiceType.TTS, ErrorCode.REQUEST_SENDING_ERROR, cause);
                player.sendSystemMessage(errorMessage.withStyle(ChatFormatting.RED));
                TouhouLittleMaid.LOGGER.error("TTS request failed: {}, error is {}", request, throwable.getMessage());
            }
            if (!isSuccessful(response)) {
                String string = new String(response.body(), StandardCharsets.UTF_8);
                String cause = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), string);
                MutableComponent errorMessage = ErrorCode.getErrorMessage(ServiceType.TTS, ErrorCode.REQUEST_RECEIVED_ERROR, cause);
                player.sendSystemMessage(errorMessage.withStyle(ChatFormatting.RED));
                TouhouLittleMaid.LOGGER.error("TTS request failed: {}, error is {}", request, cause);
            }
        });
    }
}
