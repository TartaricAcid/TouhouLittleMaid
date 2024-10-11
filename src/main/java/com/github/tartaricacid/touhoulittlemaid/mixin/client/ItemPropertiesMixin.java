package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@SuppressWarnings("all")
@Mixin(ItemProperties.class)
public class ItemPropertiesMixin {
    @Inject(method = "lambda$static$18(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;I)F", at = @At("HEAD"), cancellable = true)
    private static void onItemRender(ItemStack stack, @Nullable ClientLevel pLevel, @Nullable LivingEntity entity, int pSeed, CallbackInfoReturnable<Float> ci) {
        if (entity instanceof EntityMaid maid && maid.getMainHandItem() == stack) {
            float result = maid.hasFishingHook() ? 1.0F : 0.0F;
            ci.setReturnValue(result);
        }
    }
}
