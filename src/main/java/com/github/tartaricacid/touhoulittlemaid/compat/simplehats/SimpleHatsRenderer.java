package com.github.tartaricacid.touhoulittlemaid.compat.simplehats;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fonnymunkey.simplehats.common.item.HatItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SimpleHatsRenderer {
    static void renderHat(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, ItemStack stack, BedrockModel<Mob> model) {
        if (!stack.isEmpty() && stack.getItem() instanceof HatItem) {
            poseStack.pushPose();
            model.getHead().translateAndRotate(poseStack);
            poseStack.translate(0.0F, -0.25F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(0.63F, -0.63F, -0.63F);
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(mob, stack, ItemDisplayContext.HEAD, false, poseStack, bufferIn, packedLightIn);
            poseStack.popPose();
        }
    }

    static void renderGeckoHat(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, ItemStack stack, List<AnimatedGeoBone> model) {
        if (!stack.isEmpty() && stack.getItem() instanceof HatItem) {
            poseStack.pushPose();
            RenderUtils.prepMatrixForLocator(poseStack, model);
            poseStack.translate(0, 0.25, 0);
            poseStack.scale(0.71F, 0.71F, 0.71F);
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(mob, stack, ItemDisplayContext.HEAD, false, poseStack, bufferIn, packedLightIn);
            poseStack.popPose();
        }
    }
}
