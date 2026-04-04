package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.siliconflow.TTSSiliconflowSite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.SECRET_KEY;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.URL;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;

/**
 * SiliconFlow TTS：URL + Secret Key + 模型列表
 */
public class TTSSiliconflowFormLayout extends TTSSiteFormLayout {
    public TTSSiliconflowFormLayout(TTSSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        TTSSiliconflowSite site = (TTSSiliconflowSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_KEY, site.secretKey(), true, true)
        );
    }

    @Override
    public boolean supportsModelRows() {
        return true;
    }

    @Override
    public Map<String, String> getInitialModels() {
        return ((TTSSiliconflowSite) this.sourceSite).models();
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSSiliconflowSite site = (TTSSiliconflowSite) this.sourceSite;
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
        if (models.isEmpty()) {
            showStatus.accept(MODEL_IS_EMPTY);
            return null;
        }
        return new TTSSiliconflowSite(site.id(), site.icon(), url, site.enabled(),
                fieldValues.apply(SECRET_KEY), site.headers(), models);
    }
}
