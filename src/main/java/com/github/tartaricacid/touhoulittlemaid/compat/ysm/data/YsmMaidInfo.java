package com.github.tartaricacid.touhoulittlemaid.compat.ysm.data;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YsmMaidInfo extends YsmMaidBaseInfo {
    @Nullable
    private final ResourceLocation cacheIconId;

    public YsmMaidInfo(String modelId, String textureId, List<Component> tooltips, boolean needAuth, @Nullable ResourceLocation cacheIconId) {
        super(modelId, textureId, tooltips, needAuth);
        this.cacheIconId = cacheIconId;
    }

    @Nullable
    public ResourceLocation cacheIconId() {
        return cacheIconId;
    }
}
