package com.github.tartaricacid.touhoulittlemaid.client.resource.pojo;


import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public interface IModelInfo {
    static ResourceLocation createCacheIconId(ResourceLocation modelId) {
        String namespace = modelId.getNamespace();
        String path = modelId.getPath();
        return ResourceLocation.fromNamespaceAndPath(namespace, path + "/cache");
    }

    ResourceLocation getModelId();

    ResourceLocation getCacheIconId();

    String getName();

    ResourceLocation getModel();

    boolean isGeckoModel();

    @Nullable
    List<ResourceLocation> getAnimation();

    ResourceLocation getTexture();

    @Nullable
    List<ResourceLocation> getExtraTextures();

    List<String> getDescription();

    float getRenderItemScale();

    <T extends IModelInfo> T decorate();

    <T extends IModelInfo> T extra(ResourceLocation newModelId, ResourceLocation texture);
}
