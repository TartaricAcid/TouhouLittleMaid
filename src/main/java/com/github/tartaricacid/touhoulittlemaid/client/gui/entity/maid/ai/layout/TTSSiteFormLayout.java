package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.TTSFishAudioSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.TTSGptSovitsSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2.TTSPlayer2Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.siliconflow.TTSSiliconflowSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.TTSSystemSite;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * TTS 站点编辑器的布局策略基类
 */
public abstract class TTSSiteFormLayout {
    protected final TTSSite sourceSite;

    protected TTSSiteFormLayout(TTSSite sourceSite) {
        this.sourceSite = sourceSite;
    }

    public static TTSSiteFormLayout create(TTSSite site) {
        if (site instanceof TTSSystemSite) {
            return new TTSSystemFormLayout(site);
        }
        if (site instanceof TTSSiliconflowSite) {
            return new TTSSiliconflowFormLayout(site);
        }
        if (site instanceof TTSFishAudioSite) {
            return new TTSFishAudioFormLayout(site);
        }
        if (site instanceof TTSGptSovitsSite) {
            return new TTSGptSovitsFormLayout(site);
        }
        if (site instanceof TTSPlayer2Site) {
            return new TTSPlayer2FormLayout(site);
        }
        throw new IllegalArgumentException("Unknown TTS site type: " + site.getClass().getName());
    }

    public abstract List<FieldDescriptor> getFieldDescriptors();

    @Nullable
    public abstract TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus);

    public Map<String, String> getInitialModels() {
        return Map.of();
    }

    public boolean supportsModelRows() {
        return false;
    }

    public record FieldDescriptor(String label, String value, boolean editable, boolean secret) {
    }
}
