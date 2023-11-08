package com.github.tartaricacid.touhoulittlemaid.compat.slashblade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.capability.slashblade.CapabilitySlashBlade;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
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

    public static void renderMaidMainhandSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
        if (stack.is(SBItems.slashblade)) {
            matrixStack.pushPose();
            matrixStack.translate(0.25, 0.85, -0.75);
            matrixStack.scale(0.007F, 0.007F, 0.007F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            SlashBladeRender.renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
            matrixStack.popPose();
        }
    }

    public static void renderMaidOffhandSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
        if (stack.is(SBItems.slashblade)) {
            matrixStack.pushPose();
            matrixStack.translate(-0.25, 0.85, -0.75);
            matrixStack.scale(0.007F, 0.007F, 0.007F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            SlashBladeRender.renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
            matrixStack.popPose();
        }
    }
}
