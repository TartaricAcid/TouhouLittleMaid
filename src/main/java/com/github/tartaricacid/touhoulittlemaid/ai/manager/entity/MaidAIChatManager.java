package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.PapiReplacer;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.*;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.data.ChatTokensAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.network.message.TTSSystemAudioToClientPackage;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.AUTO_GEN_SETTING;

public final class MaidAIChatManager extends MaidAIChatData {
    public MaidAIChatManager(EntityMaid maid) {
        super(maid);
    }

    public void chat(String message, ChatClientInfo clientInfo, ServerPlayer sender) {
        if (!AIConfig.LLM_ENABLED.get()) {
            sender.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.disable")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        ChatTokensAttachment chatTokens = sender.getData(InitDataAttachment.CHAT_TOKENS);
        if (chatTokens.get() >= AIConfig.MAX_TOKENS_PER_PLAYER.get()) {
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

    // 对话起始第二个如果是 tool，剔除，确保第二个为 user 或者 assistant
    private void filterConsecutiveToolMessages(List<LLMMessage> chatCompletion) {
        if (chatCompletion.size() <= 1) {
            return;
        }

        // 保留第一个 system 消息，然后从第二个开始过滤连续的 tool 消息
        LLMMessage firstMessage = chatCompletion.getFirst();
        List<LLMMessage> filteredMessages = chatCompletion.stream()
                // 跳过第一个消息
                .skip(1)
                // 丢弃开头连续的 tool 消息
                .dropWhile(msg -> Role.TOOL.equals(msg.role()))
                .toList();

        chatCompletion.clear();
        chatCompletion.add(firstMessage);
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
            String setting = PapiReplacer.replace(chatManager.customSetting, maid, language);
            CappedQueue<LLMMessage> history = chatManager.getHistory();
            List<LLMMessage> chatList = Lists.newArrayList();
            chatList.add(LLMMessage.systemChat(maid, setting));
            // 倒序遍历，将历史对话加载进去
            history.getDeque().descendingIterator().forEachRemaining(chatList::add);
            return chatList;
        }

        // 其他情况下，获取默认设定文件
        return chatManager.getSetting().map(s -> {
            EntityMaid maid = chatManager.getMaid();
            String setting = s.getSetting(maid, language);
            CappedQueue<LLMMessage> history = chatManager.getHistory();
            List<LLMMessage> chatList = Lists.newArrayList();
            chatList.add(LLMMessage.systemChat(maid, setting));
            // 倒序遍历，将历史对话加载进去
            history.getDeque().descendingIterator().forEachRemaining(chatList::add);
            return chatList;
        }).orElse(Lists.newArrayList());
    }

    private LLMMessage autoGenSetting(EntityMaid maid, ChatClientInfo clientInfo) {
        Map<String, String> valueMap = Maps.newHashMap();
        valueMap.put("model_name", clientInfo.name());
        valueMap.put("chat_language", clientInfo.language());
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
                TTSSystemAudioToClientPackage message = new TTSSystemAudioToClientPackage(name, ttsText, config, services);
                PacketDistributor.sendToPlayer(player, message);
            }
            maid.getChatBubbleManager().addLLMChatText(chatText, waitingChatBubbleId);
        });
    }
}
