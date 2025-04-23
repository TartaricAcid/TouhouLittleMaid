package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.ChatClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response.ChatCompletionResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSRequest;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.TTSAudioToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.TTSSystemAudioToClientMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public final class MaidAIChatManager extends MaidAIChatData {
    public MaidAIChatManager(EntityMaid maid) {
        super(maid);
    }

    public void chat(String message, String language) {
        if (AIConfig.CHAT_ENABLED.get()) {
            @Nullable Site site = this.getChatSite();
            if (site == null || StringUtils.isBlank(site.getApiKey())) {
                ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.api_key.empty");
            } else {
                ChatClient chatClient = Service.getChatClient(site);
                ChatCompletion chatCompletion = Service.getChatCompletion(this, language);
                if (chatCompletion != null) {
                    chatCompletion.userChat(message);
                    chatClient.chat(chatCompletion).handle(this::onShowChatSync, this::onChatFailSync);
                    this.addUserHistory(message);
                } else {
                    ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.no_setting");
                }
            }
        } else {
            ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.disable");
        }
    }

    @SuppressWarnings("all")
    private void tts(Site site, String chatText, String ttsText) {
        // 调用系统 TTS，那么此时就只需要发送给指定的玩家即可
        if (TTSApiType.SYSTEM.getName().equals(site.getApiType())) {
            onPlaySoundLocal(chatText, ttsText);
        } else {
            TTSClient ttsClient = Service.getTtsClient(site);
            String ttsLang = "en";
            String[] split = this.getTtsLanguage().split("_");
            if (split.length >= 2) {
                ttsLang = split[0];
            }
            TTSRequest ttsRequest = Service.getTtsRequest(site, ttsText, ttsLang, this.getTtsModel());
            ttsClient.request(ttsRequest).handle(data -> onPlaySoundSync(chatText, (byte[]) data),
                    throwable -> onTtsFailSync(chatText, (Throwable) throwable));
        }
    }

    private void onShowChatSync(ChatCompletionResponse result) {
        String rawMessage = result.getFirstChoiceMessage();
        try {
            ResponseChat responseChat = Service.GSON.fromJson(rawMessage, ResponseChat.class);
            if (responseChat == null) {
                TouhouLittleMaid.LOGGER.error("Error in Response Chat: {}", rawMessage);
                onChatFailSync(Component.translatable("ai.touhou_little_maid.chat.format.json_format_error", rawMessage));
                return;
            }
            String chatText = responseChat.getChatText();
            String ttsText = responseChat.getTtsText();
            if (StringUtils.isBlank(chatText) || StringUtils.isBlank(ttsText)) {
                TouhouLittleMaid.LOGGER.error("Error in Response Chat: {}", rawMessage);
                onChatFailSync(Component.translatable("ai.touhou_little_maid.chat.format.text_is_empty", rawMessage));
                return;
            }
            this.addAssistantHistory(rawMessage);
            Site site = this.getTtsSite();
            if (AIConfig.TTS_ENABLED.get() && site != null && StringUtils.isNotBlank(site.getApiKey())) {
                this.tts(site, chatText, ttsText);
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

    private void onPlaySoundLocal(String chatText, String ttsText) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            if (maid.getOwner() instanceof ServerPlayer player) {
                NetworkHandler.sendToClientPlayer(new TTSSystemAudioToClientMessage(ttsText), player);
            }
            ChatBubbleManger.addAiChatText(maid, chatText);
        });
    }

    private void onPlaySoundSync(String chatText, byte[] data) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            NetworkHandler.sendToNearby(maid, new TTSAudioToClientMessage(this.maid.getId(), data));
            ChatBubbleManger.addAiChatText(maid, chatText);
        });
    }

    private void onTtsFailSync(String chatText, Throwable throwable) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            ChatBubbleManger.addAiChatText(maid, chatText);
            if (maid.getOwner() instanceof ServerPlayer player) {
                String cause = throwable.getLocalizedMessage();
                player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.tts.connect.fail")
                        .append(cause).withStyle(ChatFormatting.RED));
            }
        });
    }
}
