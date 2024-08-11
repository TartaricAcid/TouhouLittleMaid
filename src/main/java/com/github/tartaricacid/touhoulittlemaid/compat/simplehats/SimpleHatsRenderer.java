package com.github.tartaricacid.touhoulittlemaid.compat.simplehats;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fonnymunkey.simplehats.common.item.HatItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SimpleHatsRenderer {
    static void renderHat(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, ItemStack stack, BedrockModel<Mob> model) {
        if (!stack.isEmpty() && stack.getItem() instanceof HatItem) {
            poseStack.pushPose();
            model.getHead().translateAndRotate(poseStack);
            poseStack.translate(0.0F, -0.25F, 0.0F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            poseStack.scale(0.63F, -0.63F, -0.63F);
            Minecraft.getInstance().getItemRenderer().renderStatic(mob, stack, ItemTransforms.TransformType.HEAD, false, poseStack, bufferIn, mob.level, packedLightIn, OverlayTexture.NO_OVERLAY, mob.getId());
            poseStack.popPose();
        }
    }

    static void renderGeckoHat(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, ItemStack stack, List<AnimatedGeoBone> model) {
        if (!stack.isEmpty() && stack.getItem() instanceof HatItem) {
            poseStack.pushPose();
            RenderUtils.prepMatrixForLocator(poseStack, model);
            poseStack.translate(0, 0.25, 0);
            poseStack.scale(0.71F, 0.71F, 0.71F);
            Minecraft.getInstance().getItemRenderer().renderStatic(mob, stack, ItemTransforms.TransformType.HEAD, false, poseStack, bufferIn, mob.level, packedLightIn, OverlayTexture.NO_OVERLAY, mob.getId());
            poseStack.popPose();
        }
    }
}
