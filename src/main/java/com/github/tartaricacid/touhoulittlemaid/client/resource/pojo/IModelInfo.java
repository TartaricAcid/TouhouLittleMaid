package com.github.tartaricacid.touhoulittlemaid.client.resource.pojo;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IModelInfo {
    ResourceLocation getModelId();

    String getName();

    ResourceLocation getModel();

    List<ResourceLocation> getAnimation();

    ResourceLocation getTexture();

    List<String> getDescription();

    float getRenderItemScale();

    <T extends IModelInfo> T decorate();
}
