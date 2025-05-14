package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SystemServices;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.TTSSystemAudioToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class MaidAIChatManager extends MaidAIChatData {
    public MaidAIChatManager(EntityMaid maid) {
        super(maid);
    }

    public void chat(String message, String language) {
        if (!AIConfig.LLM_ENABLED.get()) {
            ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.disable");
        }
        @Nullable LLMSite site = this.getLLMSite();
        if (site == null || !site.enabled()) {
            ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.api_key.empty");
            return;
        }
        LLMClient chatClient = site.client();
        List<LLMMessage> chatCompletion = getChatCompletion(this, language);
        if (chatCompletion.isEmpty()) {
            ChatBubbleManger.addInnerChatText(maid, "ai.touhou_little_maid.chat.no_setting");
        }
        chatCompletion.add(LLMMessage.userChat(maid, message));
        LLMConfig config = new LLMConfig(this.getLLMModel(), AIConfig.LLM_TEMPERATURE.get(), AIConfig.LLM_MAX_TOKEN.get());
        LLMCallback callback = new LLMCallback(this, message);
        chatClient.chat(chatCompletion, config, callback);
    }

    @SuppressWarnings("all")
    public void tts(TTSSite site, String chatText, String ttsText) {
        // 调用系统 TTS，那么此时就只需要发送给指定的玩家即可
        if (site instanceof SystemServices) {
            onPlaySoundLocal(chatText, ttsText);
            return;
        }
        TTSClient ttsClient = site.client();
        String ttsModel = getTTSModel();
        String ttsLang = "en";
        String[] split = this.getTTSLanguage().split("_");
        if (split.length >= 2) {
            ttsLang = split[0];
        }
        TTSConfig config = new TTSConfig(ttsModel, ttsLang);
        TTSCallback callback = new TTSCallback(maid, chatText);
        ttsClient.play(ttsText, config, callback);
    }

    private static List<LLMMessage> getChatCompletion(MaidAIChatManager chatManager, String language) {
        // 获取设定文件
        return chatManager.getSetting().map(s -> {
            EntityMaid maid = chatManager.getMaid();
            String setting = s.getSetting(maid, language);
            CappedQueue<LLMMessage> history = chatManager.getHistory();
            List<LLMMessage> chatList = Lists.newArrayList();
            chatList.add(LLMMessage.systemChat(maid, setting));
            // 塞入一个参考回应，能让 AI 尽可能遵循参考格式进行回复
            chatList.add(LLMMessage.assistantChat(maid, "{\"chat_text\":\"看到你真开心！要不要一起去挖矿？\",\"tts_text\":\"看到你真开心！要不要一起去挖矿？\"}"));
            // 倒序遍历，将历史对话加载进去
            history.getDeque().descendingIterator().forEachRemaining(chatList::add);
            // 最后强调一下语言类型
            chatList.add(LLMMessage.userChat(maid, "请用%s语言回复 chat_text 部分！并用%s语言回复 tts_text 部分！"
                    .formatted(language, chatManager.getTTSLanguage())));
            return chatList;
        }).orElse(Collections.emptyList());
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
}
