package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class LayerMaidHeldItem extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    public LayerMaidHeldItem(EntityMaidRenderer maidRenderer) {
        super(maidRenderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack mainRightItem = maid.getMainHandItem();
        ItemStack offLeftItem = maid.getOffhandItem();
        BedrockModel<EntityMaid> model = getParentModel();
        if (!mainRightItem.isEmpty() && model.hasRightArm()) {
            this.renderArmWithItem(maid, mainRightItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
        }
        if (!offLeftItem.isEmpty() && model.hasLeftArm()) {
            this.renderArmWithItem(maid, offLeftItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private void renderArmWithItem(EntityMaid maid, ItemStack itemStack, ItemCameraTransforms.TransformType transformTypeIn, HandSide handSide, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int combinedLightIn) {
        if (!itemStack.isEmpty()) {
            matrixStack.pushPose();
            boolean isLeft = handSide == HandSide.LEFT;
            getParentModel().translateToHand(handSide, matrixStack);
            if (getParentModel().hasArmPositioningModel(handSide)) {
                getParentModel().translateToPositioningHand(handSide, matrixStack);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                matrixStack.translate(0, 0.125, -0.0625);
            } else {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                matrixStack.translate((isLeft ? -1 : 1) / 16.0, 0.125, -0.525);
            }
            Minecraft.getInstance().getItemInHandRenderer().renderItem(maid, itemStack, transformTypeIn, isLeft, matrixStack, typeBuffer, combinedLightIn);
            matrixStack.popPose();
        }
    }
}
