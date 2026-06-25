package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.mimo.STTMimoSite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.LANGUAGE;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.MODEL;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.SECRET_KEY;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.URL;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.MODEL_IS_EMPTY;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.SECRET_KEY_IS_EMPTY;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.URL_IS_EMPTY;

public class STTMimoFormLayout extends STTSiteFormLayout {
    public STTMimoFormLayout(STTSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        STTMimoSite site = (STTMimoSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_KEY, site.secretKey(), true, true),
                new FieldDescriptor(MODEL, site.model(), true, false),
                new FieldDescriptor(LANGUAGE, site.language(), true, false)
        );
    }

    @Override
    public @Nullable STTSite buildSite(Function<String, String> fieldValues, Consumer<Component> showStatus) {
        STTMimoSite site = (STTMimoSite) this.sourceSite;
        String url = StringUtils.trimToEmpty(fieldValues.apply(URL));
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        String secretKey = StringUtils.trimToEmpty(fieldValues.apply(SECRET_KEY));
        if (StringUtils.isBlank(secretKey)) {
            showStatus.accept(SECRET_KEY_IS_EMPTY);
            return null;
        }
        String model = StringUtils.trimToEmpty(fieldValues.apply(MODEL));
        if (StringUtils.isBlank(model)) {
            showStatus.accept(MODEL_IS_EMPTY);
            return null;
        }
        String language = StringUtils.defaultIfBlank(StringUtils.trimToEmpty(fieldValues.apply(LANGUAGE)), STTMimoSite.DEFAULT_LANGUAGE);
        return new STTMimoSite(site.id(), site.icon(), site.enabled(), url, secretKey, model, language);
    }
}
