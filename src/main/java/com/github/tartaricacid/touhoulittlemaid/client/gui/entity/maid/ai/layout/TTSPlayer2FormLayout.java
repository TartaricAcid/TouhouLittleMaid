package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2.TTSPlayer2Site;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.URL;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.MODEL_IS_EMPTY;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.URL_IS_EMPTY;

/**
 * Player2 TTS：URL + 模型列表（无 Secret Key）
 */
public class TTSPlayer2FormLayout extends TTSSiteFormLayout {
    public TTSPlayer2FormLayout(TTSSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        return List.of(new FieldDescriptor(URL, this.sourceSite.url(), true, false));
    }

    @Override
    public boolean supportsModelRows() {
        return true;
    }

    @Override
    public Map<String, String> getInitialModels() {
        return ((TTSPlayer2Site) this.sourceSite).models();
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSPlayer2Site site = (TTSPlayer2Site) this.sourceSite;
        String url = fieldValues.apply(URL);
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        if (models.isEmpty()) {
            showStatus.accept(MODEL_IS_EMPTY);
            return null;
        }
        return new TTSPlayer2Site(site.id(), site.icon(), url, site.enabled(), models, site.headers());
    }
}
