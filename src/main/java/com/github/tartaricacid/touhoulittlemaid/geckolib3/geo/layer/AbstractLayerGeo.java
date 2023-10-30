/*
 * Copyright: DerToaster98 - 13.06.2022
 *
 * Generic base class for more advanced layer renderers
 *
 * Originally developed for chocolate quest repoured
 */
package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.layer;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public abstract class AbstractLayerGeo<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    protected final Function<T, ResourceLocation> funcGetCurrentTexture;
    protected final Function<T, ResourceLocation> funcGetCurrentModel;

    protected GeoEntityRenderer<T> geoRendererInstance;

    public AbstractLayerGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> currentTextureFunction,
                            Function<T, ResourceLocation> currentModelFunction) {
        super(renderer);
        this.geoRendererInstance = renderer;
        this.funcGetCurrentTexture = currentTextureFunction;
        this.funcGetCurrentModel = currentModelFunction;
    }

    protected void reRenderCurrentModelInRenderer(T animatable, float partialTick, MatrixStack poseStack,
                                                  IRenderTypeBuffer bufferSource, int packedLight, RenderType renderType) {
        poseStack.pushPose();
        getRenderer().render(getEntityModel().getModel(this.funcGetCurrentModel.apply(animatable)), animatable,
                partialTick, renderType, poseStack, bufferSource, bufferSource.getBuffer(renderType), packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
