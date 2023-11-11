package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class LayerMaidHeldItem extends RenderLayer<EntityMaid, BedrockModel<EntityMaid>> {
    public LayerMaidHeldItem(EntityMaidRenderer maidRenderer) {
        super(maidRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack mainRightItem = maid.getMainHandItem();
        ItemStack offLeftItem = maid.getOffhandItem();
        BedrockModel<EntityMaid> model = getParentModel();
        if (!mainRightItem.isEmpty() && model.hasRightArm()) {
            if (SlashBladeCompat.isSlashBladeItem(mainRightItem)) {
                SlashBladeRender.renderMaidMainhandSlashBlade(maid, model, poseStack, bufferIn, packedLightIn, mainRightItem, partialTicks);
            } else {
                this.renderArmWithItem(maid, mainRightItem, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, bufferIn, packedLightIn);
            }
        }
        if (!offLeftItem.isEmpty() && model.hasLeftArm()) {
            if (SlashBladeCompat.isSlashBladeItem(offLeftItem)) {
                SlashBladeRender.renderMaidOffhandSlashBlade(model, poseStack, bufferIn, packedLightIn, offLeftItem);
            } else {
                this.renderArmWithItem(maid, offLeftItem, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, bufferIn, packedLightIn);
            }
        }
    }

    private void renderArmWithItem(EntityMaid maid, ItemStack itemStack, ItemTransforms.TransformType transformTypeIn, HumanoidArm handSide, PoseStack poseStack, MultiBufferSource typeBuffer, int combinedLightIn) {
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            boolean isLeft = handSide == HumanoidArm.LEFT;
            getParentModel().translateToHand(handSide, poseStack);
            if (getParentModel().hasArmPositioningModel(handSide)) {
                getParentModel().translateToPositioningHand(handSide, poseStack);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                poseStack.translate(0, 0.125, -0.0625);
            } else {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                poseStack.translate((isLeft ? -1 : 1) / 16.0, 0.125, -0.525);
            }
            Minecraft.getInstance().getItemInHandRenderer().renderItem(maid, itemStack, transformTypeIn, isLeft, poseStack, typeBuffer, combinedLightIn);
            poseStack.popPose();
        }
    }
}
