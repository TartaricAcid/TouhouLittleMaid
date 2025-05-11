package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.response.Message;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.response.STTAliyunCallback;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.StringUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class STTAliyunClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private final HttpClient httpClient;
    private String baseUrl = "";

    private STTAliyunClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static STTAliyunClient create(final HttpClient httpClient) {
        return new STTAliyunClient(httpClient);
    }

    public STTAliyunClient baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    @Override
    public void startRecord(Player player, EntityMaid maid) {
        List<Mixer.Info> allMicrophoneInfo = MicrophoneManager.getAllMicrophoneInfo(FORMAT);
        Mixer.Info info = allMicrophoneInfo.get(0);
        MicrophoneManager.startRecord(info.getName(), FORMAT, data -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.baseUrl))
                    .header("X-NLS-Token", AIConfig.STT_TOKEN.get())
                    .header("Content-type", "application/octet-stream")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                    .timeout(Duration.ofSeconds(15))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .whenComplete((response, throwable) -> {
                        STTAliyunCallback callback = new STTAliyunCallback(message -> handleMessage(player, maid, message));
                        if (throwable != null) {
                            callback.onFailure(request, throwable);
                            handleFail(player, throwable);
                        } else {
                            callback.onResponse(response, error -> handleFail(player, error));
                        }
                    });
        });
    }

    @Override
    public void stopRecord(Player player, EntityMaid maid) {
        MicrophoneManager.stopRecord();
    }

    private void handleFail(Player player, Throwable throwable) {
        String cause = throwable.getLocalizedMessage();
        player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.connect.fail")
                .append(cause).withStyle(ChatFormatting.RED));
    }

    private void handleMessage(Player player, EntityMaid maid, Message message) {
        String chatText = message.getResult();
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
    }
}
