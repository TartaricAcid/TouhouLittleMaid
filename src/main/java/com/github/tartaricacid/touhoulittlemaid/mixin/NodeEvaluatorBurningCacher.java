package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.IBlockBurningCacheMixin;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NodeEvaluator.class)
@SuppressWarnings("all")
public class NodeEvaluatorBurningCacher {
    @Inject(method = "isBurningBlock", at = @At("HEAD"), cancellable = true)
    private static void isBurningBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Block b = state.getBlock();
        if (b instanceof IBlockBurningCacheMixin block) {
            if (block.touhou_little_maid$isBurning() != null) {
                cir.setReturnValue(block.touhou_little_maid$isBurning());
            }
        }
    }

    @Inject(method = "isBurningBlock", at = @At("RETURN"))
    private static void postIsBurningBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Block b = state.getBlock();
        if (b instanceof IBlockBurningCacheMixin block) {
            if (block.touhou_little_maid$cannotCache() != null && block.touhou_little_maid$cannotCache()) {
                return;
            }
            Class<? extends Block> c = b.getClass();
            if (c != Block.class) {
                try {
                    c.getDeclaredMethod("is", Block.class);
                    block.touhou_little_maid$setCannotCache(true);
                    return;
                } catch (NoSuchMethodException ignored) {
                }
                try {
                    c.getDeclaredMethod("is", TagKey.class);
                    block.touhou_little_maid$setCannotCache(true);
                    return;
                } catch (NoSuchMethodException ignored) {
                }
                try {
                    c.getDeclaredMethod("is", Holder.class);
                    block.touhou_little_maid$setCannotCache(true);
                    return;
                } catch (NoSuchMethodException ignored) {
                }
                try {
                    c.getDeclaredMethod("is", HolderSet.class);
                    block.touhou_little_maid$setCannotCache(true);
                    return;
                } catch (NoSuchMethodException ignored) {
                }
            }
            block.touhou_little_maid$setBurning(cir.getReturnValue());
        }
    }
}
