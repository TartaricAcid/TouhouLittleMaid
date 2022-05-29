package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class PlaceHelper {
    private PlaceHelper() {
    }

    /**
     * 该坐标是否适合释放女仆
     *
     * @param worldIn 放置的世界
     * @param pos     放置的坐标
     * @return 是否适合放置女仆
     */
    public static boolean notSuitableForPlaceMaid(Level worldIn, BlockPos pos) {
        BlockPos firstPos = pos.above();
        BlockPos secondPos = pos.above(2);
        BlockState firstBlock = worldIn.getBlockState(firstPos);
        BlockState secondBlock = worldIn.getBlockState(secondPos);
        boolean firstBlockHasCollisionBoundingBox = hasCollisionBoundingBox(worldIn, firstBlock, firstPos);
        boolean secondBlockHasCollisionBoundingBox = hasCollisionBoundingBox(worldIn, secondBlock, secondPos);
        return firstBlockHasCollisionBoundingBox || secondBlockHasCollisionBoundingBox;
    }

    /**
     * 该坐标是否适合放置墓碑
     *
     * @param worldIn 放置的世界
     * @param pos     放置的坐标
     * @return 是否适合放置墓碑
     */
    public static boolean notSuitableForPlaceTombstone(Level worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return hasCollisionBoundingBox(worldIn, blockState, pos) && !blockState.getMaterial().isReplaceable();
    }

    /**
     * 该方块是否拥有碰撞体积
     */
    private static boolean hasCollisionBoundingBox(Level worldIn, BlockState blockState, BlockPos pos) {
        return !blockState.getCollisionShape(worldIn, pos).isEmpty();
    }
}
