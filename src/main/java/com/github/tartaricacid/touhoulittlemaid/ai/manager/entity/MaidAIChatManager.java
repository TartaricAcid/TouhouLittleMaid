package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary.HistorySummaryManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.PapiReplacer;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import com.github.tartaricacid.touhoulittlemaid.capability.ChatTokensCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.TTSSystemAudioToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.AUTO_GEN_SETTING;

public final class MaidAIChatManager extends MaidAIChatData {
    private final HistorySummaryManager historySummaryManager;

    public MaidAIChatManager(EntityMaid maid) {
        super(maid);
        this.historySummaryManager = new HistorySummaryManager(this);
    }

    @Override
    protected void onHistoryUpdated() {
        this.historySummaryManager.onHistoryUpdated();
    }

    public void chat(String message, ChatClientInfo clientInfo, ServerPlayer sender) {
        if (!AIConfig.LLM_ENABLED.get()) {
            sender.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.disable")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        sender.getCapability(ChatTokensCapabilityProvider.CHAT_TOKENS_CAP).ifPresent(chatTokens -> {
            if (chatTokens.getCount() >= AIConfig.MAX_TOKENS_PER_PLAYER.get()) {
                sender.sendSystemMessage(Component.translatable("message.touhou_little_maid.ai_chat.max_tokens_limit")
                        .withStyle(ChatFormatting.RED));
                return;
            }
            @Nullable LLMSite site = this.getLLMSite();
            if (site == null || !site.enabled()) {
                sender.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.llm.empty")
                        .withStyle(ChatFormatting.RED));
                return;
            }
            // 如果检测到是 player2，那么大概率是新手玩家，给他提示下载 player2
            if (site.id().equals(DefaultLLMSite.PLAYER2.id())) {
                Player2AppCheck.checkPlayer2App(sender, () -> this.tryToChat(message, clientInfo, site));
            } else {
                this.tryToChat(message, clientInfo, site);
            }
        });
    }

    private void tryToChat(String message, ChatClientInfo clientInfo, @NotNull LLMSite site) {
        this.chatLanguage = clientInfo.language();
        LLMClient chatClient = site.client();
        List<LLMMessage> messages = this.getMessages(this, clientInfo.language());
        if (messages.isEmpty()) {
            this.onSettingIsEmpty(clientInfo, chatClient);
        } else {
            HistoryMessagesCheck.checkMessages(messages);
            this.normalChat(message, messages, chatClient);
        }
    }

    private void normalChat(String message, List<LLMMessage> messages, LLMClient chatClient) {
        // 先插入临时的 context
        String messageWithContext = UserPromptContexts.addContext(this.maid, message);

        // http 通信添加 context
        messages.add(LLMMessage.userChat(this.maid, messageWithContext));
        // 历史记录不添加
        this.maid.getAiChatManager().addUserHistory(message);

        // 通信
        LLMCallback callback = new LLMCallback(this, messages);
        chatClient.chat(callback);
    }

    private void onSettingIsEmpty(ChatClientInfo clientInfo, LLMClient chatClient) {
        ChatBubbleManager bubbleManager = this.maid.getChatBubbleManager();
        if (AIConfig.AUTO_GEN_SETTING_ENABLED.get()) {
            List<LLMMessage> messages = this.autoGenSetting(maid, clientInfo);
            AutoGenSettingCallback callback = new AutoGenSettingCallback(this, messages);
            chatClient.chat(callback);
        } else {
            bubbleManager.addTextChatBubble("ai.touhou_little_maid.chat.llm.role_no_setting");
        }
    }

    @SuppressWarnings("all")
    public void tts(TTSSite site, String chatText, String ttsText, long waitingChatBubbleId) {
        // 调用系统 TTS，那么此时就只需要发送给指定的玩家即可
        TTSClient ttsClient = site.client();
        String ttsModel = getTTSModel();

        String ttsLang = "en";
        String[] split = this.getTTSLanguage().split("_");
        if (split.length >= 2) {
            ttsLang = split[0];
        }
        TTSConfig config = new TTSConfig(ttsModel, ttsLang);

        if (ttsClient instanceof TTSSystemServices services) {
            onPlaySoundLocal(site.id(), chatText, ttsText, config, services, waitingChatBubbleId);
        } else {
            TTSCallback callback = new TTSCallback(maid, chatText, waitingChatBubbleId);
            ttsClient.play(ttsText, config, callback);
        }
    }

    private List<LLMMessage> getMessages(MaidAIChatManager chatManager, String language) {
        // 如果含有自定义设定，则直接使用自定义设定
        if (StringUtils.isNotBlank(chatManager.customSetting)) {
            EntityMaid maid = chatManager.getMaid();
            String setting = PapiReplacer.replaceSetting(chatManager.customSetting, maid, language);
            return this.buildMessage(setting, maid, chatManager.getHistory());
        }

        // 其他情况下，获取默认设定文件
        return chatManager.getSetting().map(s -> {
            EntityMaid maid = chatManager.getMaid();
            String setting = s.getSetting(maid, language);
            return this.buildMessage(setting, maid, chatManager.getHistory());
        }).orElse(Lists.newArrayList());
    }

    /**
     * 根据女仆的设定和历史记录，构建发送给 LLM 的完整消息列表。
     * <p>
     * 最终结构为：{@code [SYSTEM 设定, SYSTEM 摘要(可选), ...历史记录(从旧到新)]}
     */
    private List<LLMMessage> buildMessage(String setting, EntityMaid maid, CappedQueue<LLMMessage> history) {
        List<LLMMessage> chatList = Lists.newArrayList();
        chatList.add(LLMMessage.systemChat(maid, setting));
        this.historySummaryManager.appendSummaryMessage(chatList);
        history.getDeque().descendingIterator().forEachRemaining(chatList::add);
        return chatList;
    }

    private List<LLMMessage> autoGenSetting(EntityMaid maid, ChatClientInfo clientInfo) {
        Map<String, String> valueMap = Util.make(Maps.newHashMap(), map -> {
            map.put("model_name", clientInfo.name());
            map.put("chat_language", clientInfo.language());
        });
        String setting = new StrSubstitutor(valueMap).replace(AUTO_GEN_SETTING);

        // 如果有描述文本，那么就将描述文本也加入到设定中
        if (!clientInfo.description().isEmpty()) {
            String join = StringUtils.join(clientInfo.description(), "\n");
            valueMap.put("model_desc", join);
            String desc = new StrSubstitutor(valueMap).replace(StringConstant.AUTO_GEN_SETTING_DESC);
            setting = setting + desc;
        }

        return Lists.newArrayList(LLMMessage.userChat(maid, setting));
    }

    private void onPlaySoundLocal(String name, String chatText, String ttsText, TTSConfig config, TTSSystemServices services, long waitingChatBubbleId) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.submit(() -> {
            if (maid.getOwner() instanceof ServerPlayer player) {
                TTSSystemAudioToClientMessage message = new TTSSystemAudioToClientMessage(name, ttsText, config, services);
                NetworkHandler.sendToClientPlayer(message, player);
            }
            maid.getChatBubbleManager().addLLMChatText(chatText, waitingChatBubbleId);
        });
    }

    public HistorySummaryManager getHistorySummaryManager() {
        return historySummaryManager;
    }
}
