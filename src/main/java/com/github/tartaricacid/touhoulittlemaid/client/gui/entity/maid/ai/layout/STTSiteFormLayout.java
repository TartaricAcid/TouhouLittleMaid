package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class STTSiteFormLayout {
    protected final STTSite sourceSite;

    protected STTSiteFormLayout(STTSite sourceSite) {
        this.sourceSite = sourceSite;
    }

    public abstract List<FieldDescriptor> getFieldDescriptors();

    @Nullable
    public abstract STTSite buildSite(Function<String, String> fieldValues, Consumer<Component> showStatus);
}
