package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary.HistorySummaryManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.PapiReplacer;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.*;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import com.github.tartaricacid.touhoulittlemaid.capability.ChatTokensCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.TTSSystemAudioToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
        LLMClient chatClient = site.client();
        List<LLMMessage> chatCompletion = getChatCompletion(this, clientInfo.language());
        if (chatCompletion.isEmpty()) {
            this.onSettingIsEmpty(message, clientInfo, chatCompletion, chatClient);
        } else {
            this.filterConsecutiveToolMessages(chatCompletion);
            this.normalChat(message, chatCompletion, chatClient);
        }
    }

    /**
     * 跳过开头的 SYSTEM 消息后，丢弃对话区开头连续的 TOOL 消息。
     * <p>
     * 历史压缩可能删掉带 tool_calls 的 ASSISTANT 消消息，导致其后的 TOOL 消息
     * 变成对话区的第一条。大多数 LLM API 要求 TOOL 消息前必须有对应的
     * ASSISTANT tool_call，否则会报错，所以需要将这些孤儿 TOOL 消息剔除。
     */
    private void filterConsecutiveToolMessages(List<LLMMessage> chatCompletion) {
        if (chatCompletion.size() <= 1) {
            return;
        }

        // 先跳过开头连续的 SYSTEM 消息，定位到"对话区"的起始位置
        int systemCount = 0;
        while (systemCount < chatCompletion.size() && chatCompletion.get(systemCount).role() == Role.SYSTEM) {
            systemCount++;
        }
        // 全都是 SYSTEM 消息，没有需要过滤的对话内容
        if (systemCount >= chatCompletion.size()) {
            return;
        }

        // 保留 SYSTEM 前缀，对话区部分丢弃开头连续的孤儿 TOOL 消息后重新拼接
        List<LLMMessage> systemMessages = Lists.newArrayList(chatCompletion.subList(0, systemCount));
        List<LLMMessage> filteredMessages = chatCompletion.stream()
                .skip(systemCount)
                // 丢弃开头连续的 tool 消息
                .dropWhile(msg -> Role.TOOL.equals(msg.role()))
                .toList();

        chatCompletion.clear();
        chatCompletion.addAll(systemMessages);
        chatCompletion.addAll(filteredMessages);
    }

    private void normalChat(String message, List<LLMMessage> chatCompletion, LLMClient chatClient) {
        ChatBubbleManager bubbleManager = this.maid.getChatBubbleManager();
        chatCompletion.add(LLMMessage.userChat(maid, message));
        LLMConfig config = LLMConfig.normalChat(this.getLLMModel(), this.maid);
        long key = bubbleManager.addThinkingText("ai.touhou_little_maid.chat.chat_bubble_waiting");
        LLMCallback callback = new LLMCallback(this, message, key);
        chatClient.chat(chatCompletion, config, callback);
    }

    private void onSettingIsEmpty(String message, ChatClientInfo clientInfo, List<LLMMessage> chatCompletion, LLMClient chatClient) {
        ChatBubbleManager bubbleManager = this.maid.getChatBubbleManager();
        if (AIConfig.AUTO_GEN_SETTING_ENABLED.get()) {
            LLMMessage llmMessage = autoGenSetting(maid, clientInfo);
            chatCompletion.add(llmMessage);
            LLMConfig config = new LLMConfig(this.getLLMModel(), this.maid, ChatType.AUTO_GEN_SETTING);
            MutableComponent component = Component.translatable("ai.touhou_little_maid.chat.llm.role_no_setting_and_gen_setting");
            TextChatBubbleData bubbleData = TextChatBubbleData.create(30 * 20, component, IChatBubbleData.TYPE_2, IChatBubbleData.DEFAULT_PRIORITY);
            long key = bubbleManager.addChatBubble(bubbleData);
            AutoGenSettingCallback callback = new AutoGenSettingCallback(this, message, key);
            chatClient.chat(chatCompletion, config, callback);
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

    private List<LLMMessage> getChatCompletion(MaidAIChatManager chatManager, String language) {
        // 如果含有自定义设定，则直接使用自定义设定
        if (StringUtils.isNotBlank(chatManager.customSetting)) {
            EntityMaid maid = chatManager.getMaid();
            String setting = PapiReplacer.replaceSetting(chatManager.customSetting, maid, language);
            return this.buildChatCompletion(setting, maid, chatManager.getHistory());
        }

        // 其他情况下，获取默认设定文件
        return chatManager.getSetting().map(s -> {
            EntityMaid maid = chatManager.getMaid();
            String setting = s.getSetting(maid, language);
            return this.buildChatCompletion(setting, maid, chatManager.getHistory());
        }).orElse(Lists.newArrayList());
    }

    /**
     * 根据女仆的设定和历史记录，构建发送给 LLM 的完整消息列表。
     * <p>
     * 最终结构为：{@code [SYSTEM 设定, SYSTEM 摘要(可选), ...历史记录(从旧到新)]}
     */
    private List<LLMMessage> buildChatCompletion(String setting, EntityMaid maid, CappedQueue<LLMMessage> history) {
        List<LLMMessage> chatList = Lists.newArrayList();
        chatList.add(LLMMessage.systemChat(maid, setting));
        this.historySummaryManager.appendSummaryMessage(chatList);
        history.getDeque().descendingIterator().forEachRemaining(chatList::add);
        return chatList;
    }

    private LLMMessage autoGenSetting(EntityMaid maid, ChatClientInfo clientInfo) {
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

        return LLMMessage.userChat(maid, setting);
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
}
