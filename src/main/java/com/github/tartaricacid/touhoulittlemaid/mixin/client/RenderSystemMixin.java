package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(at = @At("HEAD"), method = "runAsFancy(Ljava/lang/Runnable;)V")
    private static void beforeRunAsFancy(Runnable fancyRunnable, CallbackInfo ci) {
        RenderUtils.setRenderingEntitiesInInventory(true);
    }

    @Inject(at = @At("TAIL"), method = "runAsFancy(Ljava/lang/Runnable;)V")
    private static void afterRunAsFancy(Runnable fancyRunnable, CallbackInfo ci) {
        RenderUtils.setRenderingEntitiesInInventory(false);
    }
}