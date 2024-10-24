package com.github.tartaricacid.touhoulittlemaid.mixin.compat.embeddium;

import net.minecraft.client.renderer.RenderType;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
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
    @Shadow
    @Final
    private RenderType layer;

    @Inject(method = "supportsFragmentDiscard", at = @At("RETURN"), cancellable = true)
    public void supportsFragmentDiscard(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(layer == RenderType.translucent() || cir.getReturnValue());
    }
}
