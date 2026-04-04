package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.STTPlayer2Site;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.URL;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.URL_IS_EMPTY;

public class STTPlayer2FormLayout extends STTSiteFormLayout {
    public STTPlayer2FormLayout(STTSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        return List.of(new FieldDescriptor(URL, this.sourceSite.url(), true, false));
    }

    @Override
    public @Nullable STTSite buildSite(Function<String, String> fieldValues, Consumer<Component> showStatus) {
        STTPlayer2Site site = (STTPlayer2Site) this.sourceSite;
        String url = StringUtils.trimToEmpty(fieldValues.apply(URL));
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        return new STTPlayer2Site(site.id(), site.icon(), url, site.enabled(), site.headers());
    }
}
