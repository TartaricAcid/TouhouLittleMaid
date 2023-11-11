package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class GeckoLayerMaidHeld<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final FirstPersonRenderer itemInHandRenderer;

    public GeckoLayerMaidHeld(IGeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        this.itemInHandRenderer = Minecraft.getInstance().getItemInHandRenderer();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityRenderer.getGeoModel() == null) {
            return;
        }
        ItemStack offhandItem = entityLivingBaseIn.getOffhandItem();
        ItemStack mainHandItem = entityLivingBaseIn.getMainHandItem();
        GeoModel geoModel = entityRenderer.getGeoModel();
        if (!offhandItem.isEmpty() || !mainHandItem.isEmpty()) {
            matrixStackIn.pushPose();
            if (!geoModel.rightHandBones.isEmpty()) {
                if (SlashBladeCompat.isSlashBladeItem(mainHandItem)) {
                    SlashBladeRender.renderMaidMainhandSlashBlade(entityLivingBaseIn, geoModel, matrixStackIn, bufferIn, packedLightIn, mainHandItem, partialTicks);
                } else {
                    this.renderArmWithItem(entityLivingBaseIn, mainHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
                }
            }
            if (!geoModel.leftHandBones.isEmpty()) {
                if (SlashBladeCompat.isSlashBladeItem(offhandItem)) {
                    SlashBladeRender.renderMaidOffhandSlashBlade(geoModel, matrixStackIn, bufferIn, packedLightIn, offhandItem);
                } else {
                    this.renderArmWithItem(entityLivingBaseIn, offhandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
                }
            }
            matrixStackIn.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemCameraTransforms.TransformType displayContext, HandSide arm, MatrixStack matrixStack, IRenderTypeBuffer bufferSource, int light) {
        if (!itemStack.isEmpty() && this.entityRenderer.getGeoModel() != null) {
            matrixStack.pushPose();
            translateToHand(arm, matrixStack, this.entityRenderer.getGeoModel());
            matrixStack.translate(0, -0.0625, -0.1);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean isLeftHand = arm == HandSide.LEFT;
            this.itemInHandRenderer.renderItem(livingEntity, itemStack, displayContext, isLeftHand, matrixStack, bufferSource, light);
            matrixStack.popPose();
        }
    }

    protected void translateToHand(HandSide arm, MatrixStack matrixStack, GeoModel geoModel) {
        if (arm == HandSide.LEFT) {
            int size = geoModel.leftHandBones.size();
            for (int i = 0; i < size - 1; i++) {
                RenderUtils.prepMatrixForBone(matrixStack, geoModel.leftHandBones.get(i));
            }
            GeoBone lastBone = geoModel.leftHandBones.get(size - 1);
            RenderUtils.translateMatrixToBone(matrixStack, lastBone);
            RenderUtils.translateToPivotPoint(matrixStack, lastBone);
            RenderUtils.rotateMatrixAroundBone(matrixStack, lastBone);
            RenderUtils.scaleMatrixForBone(matrixStack, lastBone);
        } else {
            int size = geoModel.rightHandBones.size();
            for (int i = 0; i < size - 1; i++) {
                RenderUtils.prepMatrixForBone(matrixStack, geoModel.rightHandBones.get(i));
            }
            GeoBone lastBone = geoModel.rightHandBones.get(size - 1);
            RenderUtils.translateMatrixToBone(matrixStack, lastBone);
            RenderUtils.translateToPivotPoint(matrixStack, lastBone);
            RenderUtils.rotateMatrixAroundBone(matrixStack, lastBone);
            RenderUtils.scaleMatrixForBone(matrixStack, lastBone);
        }
    }
}
