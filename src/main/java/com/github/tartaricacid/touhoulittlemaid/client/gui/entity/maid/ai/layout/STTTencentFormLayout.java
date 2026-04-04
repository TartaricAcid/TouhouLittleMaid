package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent.STTTencentSite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.*;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;

public class STTTencentFormLayout extends STTSiteFormLayout {
    public STTTencentFormLayout(STTSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        STTTencentSite site = (STTTencentSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_ID, site.getSecretId(), true, true),
                new FieldDescriptor(SECRET_KEY, site.getSecretKey(), true, true),
                new FieldDescriptor(ENG_SER_VICE_TYPE, site.getEngSerViceType(), false, false),
                new FieldDescriptor(HOT_WORD, site.getHotWord(), true, false)
        );
    }

    @Override
    public @Nullable STTSite buildSite(Function<String, String> fieldValues, Consumer<Component> showStatus) {
        STTTencentSite site = (STTTencentSite) this.sourceSite;
        String url = StringUtils.trimToEmpty(fieldValues.apply(URL));
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        if (StringUtils.isBlank(fieldValues.apply(SECRET_ID))) {
            showStatus.accept(SECRET_ID_IS_EMPTY);
            return null;
        }
        if (StringUtils.isBlank(fieldValues.apply(SECRET_KEY))) {
            showStatus.accept(SECRET_KEY_IS_EMPTY);
            return null;
        }

        // 热词格式检查校验，只把符合格式的当做有效词，其他的丢弃，也不提示报错
        StringBuilder hotWordFixed = new StringBuilder();
        String[] split = StringUtils.split(fieldValues.apply(HOT_WORD), ',');
        for (String line : split) {
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                String word = parts[0].trim();
                String weightStr = parts[1].trim();
                if (!word.isEmpty() && weightStr.matches("\\d+")) {
                    hotWordFixed.append(word).append("|").append(weightStr).append(",");
                }
            }
        }

        return new STTTencentSite(site.id(), site.icon(), site.enabled(), url,
                StringUtils.trimToEmpty(fieldValues.apply(SECRET_ID)),
                StringUtils.trimToEmpty(fieldValues.apply(SECRET_KEY)),
                StringUtils.trimToEmpty(fieldValues.apply(ENG_SER_VICE_TYPE)),
                StringUtils.trimToEmpty(hotWordFixed.toString()));
    }
}
