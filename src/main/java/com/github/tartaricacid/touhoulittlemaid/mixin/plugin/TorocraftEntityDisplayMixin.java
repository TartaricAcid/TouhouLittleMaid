package com.github.tartaricacid.touhoulittlemaid.mixin.plugin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.display.EntityDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDisplay.class)
public class TorocraftEntityDisplayMixin {
    @Inject(at = @At("HEAD"), method = "drawEntity(Lcom/mojang/blaze3d/vertex/PoseStack;IIIFFLnet/minecraft/world/entity/LivingEntity;F)V", remap = false, cancellable = true)
    private static void getEntityInCrosshair(PoseStack matrixStack2, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, float scale, CallbackInfo ci) {
        if (entity instanceof EntityMaid) {
            ci.cancel();
        }
    }
}