package com.github.tartaricacid.touhoulittlemaid.compat.slashblade;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.capability.slashblade.CapabilitySlashBlade;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlashBladeRender {
    public static void renderSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        stack.getCapability(CapabilitySlashBlade.BLADESTATE).ifPresent(bladeState -> {
            ResourceLocation texture = bladeState.getTexture().orElse(BladeModelManager.resourceDefaultTexture);
            WavefrontObject obj = BladeModelManager.getInstance().getModel(bladeState.getModel().orElse(null));
            String part;
            if (bladeState.isBroken()) {
                part = "blade_damaged";
            } else {
                part = "blade";
            }
            BladeRenderState.renderOverrided(stack, obj, part, texture, matrixStack, bufferIn, lightIn);
            BladeRenderState.renderOverridedLuminous(stack, obj, part + "_luminous", texture, matrixStack, bufferIn, lightIn);
            BladeRenderState.renderOverrided(stack, obj, "sheath", texture, matrixStack, bufferIn, lightIn);
            BladeRenderState.renderOverridedLuminous(stack, obj, "sheath_luminous", texture, matrixStack, bufferIn, lightIn);
        });
    }

    public static void renderMaidBackSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
        matrixStack.translate(0.9, -0.25, 0.05);
        matrixStack.mulPose(Axis.ZN.rotationDegrees(15));
        matrixStack.scale(0.007F, 0.007F, 0.007F);
        renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
    }

    public static void renderMaidMainhandSlashBlade(EntityMaid maid, BedrockModel<EntityMaid> model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack, float partialTicks) {
        if (stack.is(SBItems.slashblade)) {
            matrixStack.pushPose();
            // 主手的刀渲染在左边
            if (model.hasWaistPositioningModel(HumanoidArm.LEFT)) {
                model.translateToPositioningWaist(HumanoidArm.LEFT, matrixStack);
            } else {
                matrixStack.translate(0.25, 0.85, 0);
                matrixStack.mulPose(Axis.XP.rotationDegrees(-20));
            }
            matrixStack.translate(0, 0, -0.5);
            matrixStack.scale(0.007F, 0.007F, 0.007F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            if (stack.isEmpty()) {
                return;
            }
            stack.getCapability(CapabilitySlashBlade.BLADESTATE).ifPresent(bladeState -> {
                ResourceLocation texture = bladeState.getTexture().orElse(BladeModelManager.resourceDefaultTexture);
                WavefrontObject obj = BladeModelManager.getInstance().getModel(bladeState.getModel().orElse(null));
                String part;
                if (bladeState.isBroken()) {
                    part = "blade_damaged";
                } else {
                    part = "blade";
                }
                BladeRenderState.renderOverrided(stack, obj, "sheath", texture, matrixStack, bufferIn, lightIn);
                BladeRenderState.renderOverridedLuminous(stack, obj, "sheath_luminous", texture, matrixStack, bufferIn, lightIn);
                long time = maid.level.getGameTime() - bladeState.getLastActionTime();
                if (time < 5) {
                    float i = time + partialTicks;
                    matrixStack.translate(0, 0, -0.5 / 0.007);
                    matrixStack.mulPose(Axis.YP.rotationDegrees(60 + i * 48));
                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                BladeRenderState.renderOverrided(stack, obj, part, texture, matrixStack, bufferIn, lightIn);
                BladeRenderState.renderOverridedLuminous(stack, obj, part + "_luminous", texture, matrixStack, bufferIn, lightIn);
            });
            matrixStack.popPose();
        }
    }

    public static void renderMaidOffhandSlashBlade(BedrockModel<EntityMaid> model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
        if (stack.is(SBItems.slashblade)) {
            matrixStack.pushPose();
            // 副手的刀渲染在右边
            if (model.hasWaistPositioningModel(HumanoidArm.RIGHT)) {
                model.translateToPositioningWaist(HumanoidArm.RIGHT, matrixStack);
            } else {
                matrixStack.translate(-0.25, 0.85, 0);
                matrixStack.mulPose(Axis.XP.rotationDegrees(-5));
            }
            matrixStack.translate(0, 0, -0.5);
            matrixStack.scale(0.007F, 0.007F, 0.007F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            SlashBladeRender.renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
            matrixStack.popPose();
        }
    }
}
