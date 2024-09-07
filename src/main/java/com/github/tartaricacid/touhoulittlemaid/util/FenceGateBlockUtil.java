package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.mixin.FenceGateBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class FenceGateBlockUtil {

    public static boolean isOpen(BlockState blockState) {
       return blockState.getBlock() instanceof FenceGateBlock && blockState.getValue(FenceGateBlock.OPEN);
    }

    public static void setOpen(@Nullable Entity entity, Level level, BlockState blockState, BlockPos blockPos, boolean open) {
        if (blockState.getBlock() instanceof FenceGateBlock fenceGateBlock && blockState.getValue(FenceGateBlock.OPEN) != open) {
            level.setBlock(blockPos, blockState.setValue(FenceGateBlock.OPEN, open), 10);
            if (entity != null) {
                entity.playSound(open ? ((FenceGateBlockAccessor)fenceGateBlock).tlmOpenSound() : ((FenceGateBlockAccessor)fenceGateBlock).tlmCloseSound(),  1.0f, 1.0f);
            }
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
        }
    }
}
