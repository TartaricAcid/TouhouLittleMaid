package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.STTAliyunSite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.*;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;

public class STTAliyunFormLayout extends STTSiteFormLayout {
    public STTAliyunFormLayout(STTSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        STTAliyunSite site = (STTAliyunSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.getBaseUrl(), true, false),
                new FieldDescriptor(SECRET_KEY, site.getSecretKey(), true, true),
                new FieldDescriptor(APP_KEY, site.getAppKey(), true, false)
        );
    }

    @Override
    public @Nullable STTSite buildSite(Function<String, String> fieldValues, Consumer<Component> showStatus) {
        STTAliyunSite site = (STTAliyunSite) this.sourceSite;
        String baseUrl = StringUtils.trimToEmpty(fieldValues.apply(URL));
        if (StringUtils.isBlank(baseUrl)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        if (StringUtils.isBlank(fieldValues.apply(SECRET_KEY))) {
            showStatus.accept(SECRET_KEY_IS_EMPTY);
            return null;
        }
        if (StringUtils.isBlank(fieldValues.apply(APP_KEY))) {
            showStatus.accept(APP_KEY_IS_EMPTY);
            return null;
        }
        return new STTAliyunSite(site.id(), site.icon(), site.enabled(), baseUrl,
                StringUtils.trimToEmpty(fieldValues.apply(SECRET_KEY)),
                StringUtils.trimToEmpty(fieldValues.apply(APP_KEY)),
                site.getVocabularyId(),
                site.getCustomizationId(),
                site.isEnablePunctuationPrediction(),
                site.isEnableInverseTextNormalization(),
                site.isEnableVoiceDetection(),
                site.isDisfluency());
    }
}
