package com.github.tartaricacid.touhoulittlemaid.compat.ysm.data;

import net.minecraft.network.chat.Component;

import java.util.List;

public class YsmMaidBaseInfo {
    private final String modelId;
    private final String textureId;
    private final List<Component> tooltips;
    private final boolean needAuth;

    public YsmMaidBaseInfo(String modelId, String textureId, List<Component> tooltips, boolean needAuth) {
        this.modelId = modelId;
        this.textureId = textureId;
        this.tooltips = tooltips;
        this.needAuth = needAuth;
    }

    public String modelId() {
        return modelId;
    }

    public String textureId() {
        return textureId;
    }

    public List<Component> tooltips() {
        return tooltips;
    }

    public boolean needAuth() {
        return needAuth;
    }
}
