package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.TTSMiniMaxSite;
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
 * MiniMax TTS：URL + Secret Key + 模型 + 声音列表
 */
public class TTSMiniMaxFormLayout extends TTSSiteFormLayout {
    public TTSMiniMaxFormLayout(TTSSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        TTSMiniMaxSite site = (TTSMiniMaxSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_KEY, site.secretKey(), true, true),
                new FieldDescriptor(MODEL, site.siteModel(), true, false)
        );
    }

    @Override
    public boolean supportsModelRows() {
        return true;
    }

    @Override
    public Map<String, String> getInitialModels() {
        return ((TTSMiniMaxSite) this.sourceSite).models();
    }

    @Override
    public MutableComponent modelsTitle() {
        return VOICES_NAME;
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSMiniMaxSite site = (TTSMiniMaxSite) this.sourceSite;
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
        String siteModel = fieldValues.apply(MODEL);
        if (StringUtils.isBlank(siteModel)) {
            showStatus.accept(MODEL_IS_EMPTY);
            return null;
        }
        if (models.isEmpty()) {
            showStatus.accept(VOICE_IS_EMPTY);
            return null;
        }
        return new TTSMiniMaxSite(site.id(), site.icon(), url, site.enabled(),
                fieldValues.apply(SECRET_KEY), siteModel, site.headers(), models);
    }
}
