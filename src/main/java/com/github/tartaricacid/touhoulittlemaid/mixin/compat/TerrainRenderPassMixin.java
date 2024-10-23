package com.github.tartaricacid.touhoulittlemaid.mixin.compat;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(TerrainRenderPass.class)
public class TerrainRenderPassMixin {
    @Shadow @Final private RenderType renderType;

    @Inject(method = "supportsFragmentDiscard", at = @At("RETURN"), cancellable = true)
    public void supportsFragmentDiscard(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(renderType == RenderType.translucent() || cir.getReturnValue());
    }
}
