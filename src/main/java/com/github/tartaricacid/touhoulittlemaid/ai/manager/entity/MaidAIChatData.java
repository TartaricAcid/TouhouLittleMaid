package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.CharacterSetting;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.SettingReader;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.SystemSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.CappedQueue;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("all")
public abstract class MaidAIChatData extends MaidAIChatSerializable {
    protected final EntityMaid maid;
    protected final CappedQueue<LLMMessage> history;

    public MaidAIChatData(EntityMaid maid) {
        this.maid = maid;
        this.history = new CappedQueue<>(AIConfig.MAID_MAX_HISTORY_LLM_SIZE.get());
    }

    @Nullable
    public LLMSite getLLMSite() {
        LLMSite site;
        if (StringUtils.isBlank(llmSite)) {
            site = DefaultLLMSite.PLAYER2;
        } else {
            site = AvailableSites.getLLMSite(llmSite);
            if (site == null || !site.enabled()) {
                site = DefaultLLMSite.PLAYER2;
            }
        }
        return site;
    }

    @Nullable
    public TTSSite getTTSSite() {
        TTSSite site;
        if (StringUtils.isBlank(ttsSite)) {
            site = AvailableSites.getTTSSite(SystemSite.API_TYPE);
        } else {
            site = AvailableSites.getTTSSite(ttsSite);
            if (site == null || !site.enabled()) {
                site = AvailableSites.getTTSSite(SystemSite.API_TYPE);
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

    public CappedQueue<LLMMessage> getHistory() {
        return history;
    }

    public void addUserHistory(String message) {
        this.history.add(LLMMessage.userChat(maid, message));
    }

    public void addAssistantHistory(String message) {
        this.history.add(LLMMessage.assistantChat(maid, message));
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public Optional<CharacterSetting> getSetting() {
        String modelId = this.maid.getModelId();
        return SettingReader.getSetting(modelId);
    }
}
