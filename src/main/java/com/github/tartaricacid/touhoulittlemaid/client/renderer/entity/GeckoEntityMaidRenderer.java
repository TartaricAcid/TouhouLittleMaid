package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;

public class GeckoEntityMaidRenderer<T extends Mob> extends GeoReplacedEntityRenderer<T, GeckoMaidEntity<T>> {
    public GeckoEntityMaidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        addLayer(new GeckoLayerMaidHeld<>(this, renderManager.getItemInHandRenderer()));
        addLayer(new GeckoLayerMaidBipedHead<>(this, renderManager.getModelSet()));
        addLayer(new GeckoLayerMaidBackpack<>(this, renderManager.getModelSet()));
        addLayer(new GeckoLayerMaidBackItem<>(this));
        addLayer(new GeckoLayerMaidBanner<>(this, renderManager.getModelSet()));
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.hasData(GeckoMaidEntity.TYPE)) {
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public GeckoMaidEntity<T> getAnimatableEntity(T entity) {
        return entity.getData(GeckoMaidEntity.TYPE);
    }

    @Override
    public float getHeightScale(T entity) {
        return getAnimatableEntity(entity).getMaidInfo().getRenderEntityScale();
    }

    @Override
    public float getWidthScale(T entity) {
        return getAnimatableEntity(entity).getMaidInfo().getRenderEntityScale();
    }
}
