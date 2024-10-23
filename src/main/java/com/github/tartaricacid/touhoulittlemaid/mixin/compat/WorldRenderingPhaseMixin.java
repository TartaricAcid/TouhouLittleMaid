package com.github.tartaricacid.touhoulittlemaid.mixin.compat;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import net.irisshaders.iris.pipeline.WorldRenderingPhase;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(WorldRenderingPhase.class)
public class WorldRenderingPhaseMixin {
    @Inject(method = "fromTerrainRenderType", at = @At("HEAD"), cancellable = true)
    private static void fromTerrainRenderType(RenderType renderType, CallbackInfoReturnable<WorldRenderingPhase> cir) {
        if (DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.containsValue(renderType)) {
            cir.setReturnValue(WorldRenderingPhase.TERRAIN_CUTOUT_MIPPED);
            return;
        }

        if (DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.containsValue(renderType)) {
            cir.setReturnValue(WorldRenderingPhase.TERRAIN_TRANSLUCENT);
        }
    }
}
