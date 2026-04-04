package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.TTSSystemSite;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 系统 TTS：无可编辑字段，仅显示提示文本
 */
public class TTSSystemFormLayout extends TTSSiteFormLayout {
    public TTSSystemFormLayout(TTSSite sourceSite) {
        super(sourceSite);
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        return List.of();
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSSystemSite site = (TTSSystemSite) this.sourceSite;
        return new TTSSystemSite(site.id(), site.icon(), site.enabled());
    }
}
