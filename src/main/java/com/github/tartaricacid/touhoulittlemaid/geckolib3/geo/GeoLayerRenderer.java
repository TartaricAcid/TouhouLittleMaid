package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.GeoModelProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unchecked")
public abstract class GeoLayerRenderer<T extends Entity & IAnimatable> {
    protected final IGeoRenderer<T> entityRenderer;

    public GeoLayerRenderer(IGeoRenderer<T> entityRendererIn) {
        this.entityRenderer = entityRendererIn;
    }

    protected void renderCopyModel(GeoModelProvider<T> modelProvider,
                                   ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource,
                                   int packedLight, T animatable, float partialTick, float red, float green, float blue) {
        if (!animatable.isInvisible()) {
            renderModel(modelProvider, texture, poseStack, bufferSource, packedLight, animatable,
                    partialTick, red, green, blue);
        }
    }

    protected void renderModel(GeoModelProvider<T> modelProvider,
                               ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource,
                               int packedLight, T animatable, float partialTick, float red, float green, float blue) {
        if (animatable instanceof LivingEntity entity) {
            RenderType renderType = getRenderType(texture);
            getRenderer().render(modelProvider.getModel(modelProvider.getModelLocation(animatable)),
                    animatable, partialTick, renderType, poseStack, bufferSource, bufferSource.getBuffer(renderType),
                    packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0), red, green, blue, 1);
        }
    }

    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.entityCutout(textureLocation);
    }

    public GeoModelProvider<T> getEntityModel() {
        return this.entityRenderer.getGeoModelProvider();
    }

    public IGeoRenderer<T> getRenderer() {
        return this.entityRenderer;
    }

    protected ResourceLocation getEntityTexture(T entityIn) {
        return this.entityRenderer.getTextureLocation(entityIn);
    }


    public abstract void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                                T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch);
}