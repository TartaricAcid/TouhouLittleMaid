package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeoLayerMaidRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public interface IGeoEntityRenderer<T extends Entity> {
    IGeoEntity getGeoEntityRender(T entity);

    void addGeoMobLayer(GeoLayerMaidRender<?, ?> geoLayerMaidRender2);

    void geoRender(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight);
}
