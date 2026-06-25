package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo.TTSMimoSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.*;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;

/**
 * Xiaomi Mimo TTS：URL + API Key + 模型 + 音色/风格提示词 + 声音列表
 */
public class TTSMimoFormLayout extends TTSSiteFormLayout {
    private String siteModelValue;
    private String voiceCloneInputModeValue;
    private String voiceCloneMimeTypeValue;

    public TTSMimoFormLayout(TTSSite sourceSite) {
        super(sourceSite);
        TTSMimoSite site = (TTSMimoSite) sourceSite;
        this.siteModelValue = site.siteModel();
        this.voiceCloneInputModeValue = site.voiceCloneInputMode();
        this.voiceCloneMimeTypeValue = site.voiceCloneMimeType();
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        TTSMimoSite site = (TTSMimoSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_KEY, site.secretKey(), true, true),
                new FieldDescriptor(CUSTOM_MODEL, site.customModel(), true, false),
                new FieldDescriptor(VOICE_PROMPT, site.voicePrompt(), true, false),
                new FieldDescriptor(VOICE_CLONE_AUDIO, site.voiceCloneAudio(), true, false)
        );
    }

    @Override
    public int extraInit(int x, int y, int width, TTSSiteEditorScreen screen) {
        int halfWidth = (width - 6) / 2;

        screen.addRenderableWidget(new FlatColorButton(x, y + 2, width, 18,
                this.siteModelName(), b -> b.setMessage(this.nextSiteModel())));

        screen.addRenderableWidget(new FlatColorButton(x, y + 25, halfWidth, 18,
                this.voiceCloneInputModeName(), b -> b.setMessage(this.nextVoiceCloneInputMode())));

        screen.addRenderableWidget(new FlatColorButton(x + halfWidth + 6, y + 25, halfWidth, 18,
                this.voiceCloneMimeTypeName(), b -> b.setMessage(this.nextVoiceCloneMimeType())));

        return 58;
    }

    @Override
    public int extraInitAfterFieldRows() {
        return 1;
    }

    @Override
    public boolean supportsModelRows() {
        return true;
    }

    @Override
    public Map<String, String> getInitialModels() {
        return ((TTSMimoSite) this.sourceSite).models();
    }

    @Override
    public MutableComponent modelsTitle() {
        return VOICES_NAME;
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSMimoSite site = (TTSMimoSite) this.sourceSite;
        String url = fieldValues.apply(URL);
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        String secretKey = fieldValues.apply(SECRET_KEY);
        if (StringUtils.isBlank(secretKey)) {
            showStatus.accept(SECRET_KEY_IS_EMPTY);
            return null;
        }
        String customModel = StringUtils.trimToEmpty(fieldValues.apply(CUSTOM_MODEL));
        String siteModel = StringUtils.defaultIfBlank(customModel, this.siteModelValue);
        String voicePrompt = fieldValues.apply(VOICE_PROMPT);
        if (TTSMimoSite.isVoiceDesignModel(siteModel) && StringUtils.isBlank(voicePrompt)) {
            showStatus.accept(VOICE_PROMPT_IS_EMPTY);
            return null;
        }
        String voiceCloneAudio = fieldValues.apply(VOICE_CLONE_AUDIO);
        if (TTSMimoSite.isVoiceCloneModel(siteModel) && StringUtils.isBlank(voiceCloneAudio)) {
            showStatus.accept(VOICE_CLONE_AUDIO_IS_EMPTY);
            return null;
        }
        if (TTSMimoSite.isVoiceCloneModel(siteModel)
                && TTSMimoSite.VOICE_CLONE_MODE_FILE.equals(this.voiceCloneInputModeValue)) {
            voiceCloneAudio = TTSMimoSite.normalizeVoiceCloneFilePath(voiceCloneAudio);
        }
        if (models.isEmpty()) {
            showStatus.accept(VOICE_IS_EMPTY);
            return null;
        }
        return new TTSMimoSite(site.id(), site.icon(), url, site.enabled(),
                secretKey, this.siteModelValue, customModel, voicePrompt,
                this.voiceCloneInputModeValue, this.voiceCloneMimeTypeValue, voiceCloneAudio,
                site.headers(), models);
    }

    private MutableComponent siteModelName() {
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.mimo.tts_model.%s".formatted(this.siteModelValue));
    }

    private MutableComponent nextSiteModel() {
        int index = TTSMimoSite.presetModels().indexOf(this.siteModelValue);
        if (index == -1) {
            this.siteModelValue = TTSMimoSite.MODEL_TTS;
        } else {
            int nextIndex = (index + 1) % TTSMimoSite.presetModels().size();
            this.siteModelValue = TTSMimoSite.presetModels().get(nextIndex);
        }
        return this.siteModelName();
    }

    private MutableComponent voiceCloneInputModeName() {
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.mimo.voice_clone_input_mode.%s".formatted(this.voiceCloneInputModeValue));
    }

    private MutableComponent nextVoiceCloneInputMode() {
        int index = TTSMimoSite.voiceCloneModes().indexOf(this.voiceCloneInputModeValue);
        if (index == -1) {
            this.voiceCloneInputModeValue = TTSMimoSite.VOICE_CLONE_MODE_BASE64;
        } else {
            int nextIndex = (index + 1) % TTSMimoSite.voiceCloneModes().size();
            this.voiceCloneInputModeValue = TTSMimoSite.voiceCloneModes().get(nextIndex);
        }
        return this.voiceCloneInputModeName();
    }

    private MutableComponent voiceCloneMimeTypeName() {
        String key = TTSMimoSite.MIME_AUDIO_MPEG.equals(this.voiceCloneMimeTypeValue) ? "audio_mpeg" : "audio_wav";
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.mimo.voice_clone_mime_type.%s".formatted(key));
    }

    private MutableComponent nextVoiceCloneMimeType() {
        int index = TTSMimoSite.voiceCloneMimeTypes().indexOf(this.voiceCloneMimeTypeValue);
        if (index == -1) {
            this.voiceCloneMimeTypeValue = TTSMimoSite.MIME_AUDIO_WAV;
        } else {
            int nextIndex = (index + 1) % TTSMimoSite.voiceCloneMimeTypes().size();
            this.voiceCloneMimeTypeValue = TTSMimoSite.voiceCloneMimeTypes().get(nextIndex);
        }
        return this.voiceCloneMimeTypeName();
    }
}
