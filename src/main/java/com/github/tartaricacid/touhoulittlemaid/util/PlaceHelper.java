package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/8/19 14:35
 **/
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
    public static boolean notSuitableForPlaceMaid(World worldIn, BlockPos pos) {
        BlockPos firstPos = pos.up();
        BlockPos secondPos = pos.up(2);
        IBlockState firstBlock = worldIn.getBlockState(firstPos);
        IBlockState secondBlock = worldIn.getBlockState(secondPos);
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
    public static boolean notSuitableForPlaceTombstone(World worldIn, BlockPos pos) {
        IBlockState blockState = worldIn.getBlockState(pos);
        return hasCollisionBoundingBox(worldIn, blockState, pos);
    }

    /**
     * 该方块是否拥有碰撞体积
     */
    private static boolean hasCollisionBoundingBox(World worldIn, IBlockState blockState, BlockPos pos) {
        return blockState.getCollisionBoundingBox(worldIn, pos) != Block.NULL_AABB;
    }
}
