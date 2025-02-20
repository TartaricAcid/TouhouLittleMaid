package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public abstract class GeoLayerRenderer<T extends Entity, R extends IGeoEntityRenderer<T>> {
    protected final R entityRenderer;

    public GeoLayerRenderer(R entityRendererIn) {
        this.entityRenderer = entityRendererIn;
    }

    public R getRenderer() {
        return this.entityRenderer;
    }

    public IGeoEntity getGeoEntity(T entity) {
        return this.getRenderer().getGeoEntity(entity);
    }

    protected ILocationModel getLocationModel(T entity) {
        return this.getGeoEntity(entity).getGeoModel();
    }

    public abstract GeoLayerRenderer<T, R> copy(R entityRendererIn);

    public abstract void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch);
}