package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class CacheIconTexture extends AbstractTexture {
    private final ResourceLocation modelId;
    private NativeImage imageIn;

    public CacheIconTexture(ResourceLocation modelId, NativeImage imageIn) {
        this.modelId = modelId;
        this.imageIn = imageIn;
    }

    @Override
    public void load(ResourceManager manager) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(this::doLoad);
        } else {
            this.doLoad();
        }
    }

    private void doLoad() {
        if (imageIn == null) {
            return;
        }
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        TextureUtil.prepareImage(this.getId(), 0, width, height);
        imageIn.upload(0, 0, 0, 0, 0, width, height, false, false, false, true);
        // 释放内存
        imageIn = null;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
