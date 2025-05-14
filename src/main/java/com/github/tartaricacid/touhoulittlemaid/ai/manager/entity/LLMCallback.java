package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpRequest;

public class LLMCallback implements ResponseCallback<String> {
    private final EntityMaid maid;
    private final MaidAIChatManager chatManager;
    private final String message;

    public LLMCallback(MaidAIChatManager chatManager, String message) {
        this.maid = chatManager.getMaid();
        this.chatManager = chatManager;
        this.message = message;
    }

    @Override
    public void onFailure(HttpRequest request, Throwable e) {
        this.onChatFailSync(e);
        TouhouLittleMaid.LOGGER.error("LLM request failed: {}, error is {}", request, e.getMessage());
    }

    @Override
    public void onSuccess(String response) {
        try {
            ResponseChat responseChat = Client.GSON.fromJson(response, ResponseChat.class);
            if (responseChat == null) {
                TouhouLittleMaid.LOGGER.error("Error in Response Chat: {}", response);
                onChatFailSync(Component.translatable("ai.touhou_little_maid.chat.format.json_format_error", response));
                return;
            }

            String chatText = responseChat.getChatText();
            String ttsText = responseChat.getTtsText();
            if (StringUtils.isBlank(chatText) || StringUtils.isBlank(ttsText)) {
                TouhouLittleMaid.LOGGER.error("Error in Response Chat: {}", response);
                onChatFailSync(Component.translatable("ai.touhou_little_maid.chat.format.text_is_empty", response));
                return;
            }

            // 缓存历史聊天记录
            chatManager.addUserHistory(message);
            chatManager.addAssistantHistory(response);

            TTSSite site = chatManager.getTTSSite();
            if (AIConfig.TTS_ENABLED.get() && site != null && site.enabled()) {
                chatManager.tts(site, chatText, ttsText);
            } else {
                ChatBubbleManger.addAiChatTextSync(maid, chatText);
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error(e.getMessage());
        }
    }

    private void onChatFailSync(Throwable throwable) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            if (maid.getOwner() instanceof ServerPlayer player) {
                String cause = throwable.getLocalizedMessage();
                player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.connect.fail")
                        .append(cause).withStyle(ChatFormatting.RED));
            }
        });
    }

    private void onChatFailSync(Component message) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            if (maid.getOwner() instanceof ServerPlayer player) {
                player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.connect.fail")
                        .append(message).withStyle(ChatFormatting.RED));
            }
        });
    }
}
