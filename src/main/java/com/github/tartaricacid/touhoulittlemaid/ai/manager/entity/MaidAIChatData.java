package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.CharacterSetting;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.SettingReader;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.TTSSystemSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@SuppressWarnings("all")
public abstract class MaidAIChatData extends MaidAIChatSerializable {
    protected static final String MAID_HISTORY_CHAT_TAG = "MaidHistoryChat";
    protected static final String MAID_HISTORY_SUMMARY_TAG = "MaidHistorySummary";
    protected static final String MAID_LAST_CHAT_TOKEN_USAGE_TAG = "MaidLastChatTokenUsage";

    protected final EntityMaid maid;
    protected final CappedQueue<LLMMessage> history;

    protected String compressedSummary = StringUtils.EMPTY;
    protected volatile int lastChatTokenUsage = 0;
    public volatile boolean historySummaryRunning = false;

    public MaidAIChatData(EntityMaid maid) {
        this.maid = maid;
        this.history = new CappedQueue<>(512);
    }

    @Override
    public CompoundTag readFromTag(CompoundTag tag) {
        if (tag.contains(MAID_HISTORY_CHAT_TAG)) {
            try {
                this.history.getDeque().clear();
                LLMMessage.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get(MAID_HISTORY_CHAT_TAG))
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .ifPresent(list -> {
                            ListIterator<LLMMessage> iterator = list.listIterator(list.size());
                            while (iterator.hasPrevious()) {
                                history.add(iterator.previous());
                            }
                        });
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to parse MaidHistoryChat", e);
            }
        }
        this.compressedSummary = tag.getString(MAID_HISTORY_SUMMARY_TAG);
        this.lastChatTokenUsage = tag.getInt(MAID_LAST_CHAT_TOKEN_USAGE_TAG);
        return super.readFromTag(tag);
    }

    @Override
    public CompoundTag writeToTag(CompoundTag tag) {
        if (this.history.size() > 0) {
            try {
                ArrayList<LLMMessage> llmMessages = Lists.newArrayList(this.history.getDeque());
                LLMMessage.CODEC.listOf().encodeStart(NbtOps.INSTANCE, llmMessages)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .ifPresent(t -> tag.put(MAID_HISTORY_CHAT_TAG, t));
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to parse MaidHistoryChat", e);
            }
        }
        if (StringUtils.isNotBlank(this.compressedSummary)) {
            tag.putString(MAID_HISTORY_SUMMARY_TAG, this.compressedSummary);
        }
        if (this.lastChatTokenUsage > 0) {
            tag.putInt(MAID_LAST_CHAT_TOKEN_USAGE_TAG, this.lastChatTokenUsage);
        }
        return super.writeToTag(tag);
    }

    @Nullable
    public LLMSite getLLMSite() {
        LLMSite site;
        if (StringUtils.isBlank(llmSite)) {
            site = getDefaultLLMSite();
        } else {
            site = AvailableSites.getLLMSite(llmSite);
            if (site == null || !site.enabled()) {
                site = getDefaultLLMSite();
            }
        }
        return site;
    }

    private LLMSite getDefaultLLMSite() {
        LLMSite site = AvailableSites.getLLMSite(DefaultLLMSite.DEEPSEEK.id());
        return site == null ? DefaultLLMSite.DEEPSEEK : site;
    }

    @Nullable
    public TTSSite getTTSSite() {
        if (isNoTTSSite(ttsSite)) {
            return null;
        }

        TTSSite site;
        if (StringUtils.isBlank(ttsSite)) {
            site = AvailableSites.getTTSSite(TTSSystemSite.API_TYPE);
        } else {
            site = AvailableSites.getTTSSite(ttsSite);
            if (site == null || !site.enabled()) {
                site = AvailableSites.getTTSSite(TTSSystemSite.API_TYPE);
            }
        }
        return site;
    }

    public String getLLMModel() {
        LLMSite site = getLLMSite();
        String model = StringUtils.EMPTY;
        if (site instanceof SupportModelSelect select) {
            if (StringUtils.isBlank(llmModel)) {
                model = select.getDefaultModel();
            } else {
                model = select.getModel(llmModel);
            }
        }
        return model;
    }

    public String getTTSModel() {
        TTSSite site = getTTSSite();
        String model = StringUtils.EMPTY;
        if (site instanceof SupportModelSelect select) {
            if (StringUtils.isBlank(ttsModel)) {
                model = select.getDefaultModel();
            } else {
                model = select.getModel(ttsModel);
            }
        }
        return model;
    }

    public String getTTSLanguage() {
        if (StringUtils.isNotBlank(ttsLanguage)) {
            return ttsLanguage;
        }
        return AIConfig.TTS_LANGUAGE.get();
    }

    public String getChatLanguage() {
        if (StringUtils.isNotBlank(chatLanguage)) {
            return chatLanguage;
        }
        return "en_us";
    }

    public CappedQueue<LLMMessage> getHistory() {
        return history;
    }

    public String getCompressedSummary() {
        return compressedSummary;
    }

    public void setCompressedSummary(String newSummary) {
        this.compressedSummary = newSummary;
    }

    public boolean hasCompressedSummary() {
        return StringUtils.isNotBlank(compressedSummary);
    }

    public int getLastChatTokenUsage() {
        return lastChatTokenUsage;
    }

    public void setLastChatTokenUsage(int lastChatTokenUsage) {
        this.lastChatTokenUsage = Math.max(0, lastChatTokenUsage);
    }

    public void clearAllChatMemory() {
        this.history.getDeque().clear();
        this.compressedSummary = StringUtils.EMPTY;
        this.lastChatTokenUsage = 0;
        this.historySummaryRunning = false;
    }

    public void addUserHistory(String message) {
        this.history.add(LLMMessage.userChat(maid, message));
        this.onHistoryUpdated();
    }

    public void addAssistantHistory(String message) {
        this.history.add(LLMMessage.assistantChat(maid, message));
        this.onHistoryUpdated();
    }

    public void addAssistantHistory(String message, List<ToolCall> toolCalls) {
        this.history.add(LLMMessage.assistantChat(maid, message, toolCalls));
        this.onHistoryUpdated();
    }

    public void addToolHistory(String message, String toolCallId) {
        this.history.add(LLMMessage.toolChat(maid, message, toolCallId));
        this.onHistoryUpdated();
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public Optional<CharacterSetting> getSetting() {
        String modelId = this.maid.getModelId();
        return SettingReader.getSetting(modelId);
    }

    protected void onHistoryUpdated() {
    }
}
