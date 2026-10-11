package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation.BurningStateCache;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public class NodeEvaluatorBurningCacher {
    @Inject(method = "isBurningBlock", at = @At("HEAD"), cancellable = true)
    private static void isBurningBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        BurningStateCache cache = BurningStateCache.get();
        if (cache != null) {
            cir.setReturnValue(cache.isBurning(state));
        }
    }
}
