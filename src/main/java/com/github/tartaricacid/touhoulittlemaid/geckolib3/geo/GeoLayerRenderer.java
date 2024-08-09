package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public abstract class GeoLayerRenderer<T extends Entity, R extends IGeoRenderer<T>> {
    protected final R entityRenderer;

    public GeoLayerRenderer(R entityRendererIn) {
        this.entityRenderer = entityRendererIn;
    }

    public IGeoRenderer<T> getRenderer() {
        return this.entityRenderer;
    }

    public abstract void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                                T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch);
}