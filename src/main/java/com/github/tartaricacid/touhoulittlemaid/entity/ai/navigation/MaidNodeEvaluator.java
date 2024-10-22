package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * 该方法仅修改了栅栏门的寻路判断
 */
public class MaidNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public PathType getPathType(PathfindingContext pContext, int pX, int pY, int pZ) {
        return getMaidBlockPathTypeStatic(pContext, new BlockPos.MutableBlockPos(pX, pY, pZ));
    }

    private PathType getMaidBlockPathTypeStatic(PathfindingContext context, BlockPos.MutableBlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        PathType pathType = getMaidBlockPathTypeRaw(context, x, y, z);
        if (pathType == PathType.OPEN && y >= context.level().getMinBuildHeight() + 1) {
            return switch (getMaidBlockPathTypeRaw(context, x, y - 1, z)) {
                case OPEN, WATER, LAVA, WALKABLE -> PathType.OPEN;
                case DAMAGE_FIRE -> PathType.DAMAGE_FIRE;
                case DAMAGE_OTHER -> PathType.DAMAGE_OTHER;
                case STICKY_HONEY -> PathType.STICKY_HONEY;
                case POWDER_SNOW -> PathType.DANGER_POWDER_SNOW;
                case DAMAGE_CAUTIOUS -> PathType.DAMAGE_CAUTIOUS;
                case TRAPDOOR -> PathType.DANGER_TRAPDOOR;
                default -> checkNeighbourBlocks(context, x, y, z, PathType.WALKABLE);
            };
        } else {
            return pathType;
        }
    }

    private PathType getMaidBlockPathTypeRaw(PathfindingContext context, int pX, int pY, int pZ) {
        BlockPos pos = new BlockPos(pX, pY, pZ);
        BlockState blockState = context.getBlockState(pos);
        PathType pathType;
        if (blockState.getBlock() instanceof FenceGateBlock) {
            pathType = blockState.getValue(FenceGateBlock.OPEN) ? PathType.DOOR_OPEN : PathType.DOOR_WOOD_CLOSED;
        } else {
            pathType = context.getPathTypeFromState(pX, pY, pZ);
        }
        if (pathType == PathType.DOOR_WOOD_CLOSED && this.mob instanceof EntityMaid maid && !this.canOpenDoor(blockState.getBlock(), maid)) {
            pathType = PathType.DOOR_IRON_CLOSED;
        }
        return pathType;
    }

    private boolean canOpenDoor(Block block, EntityMaid maid) {
        if (block instanceof DoorBlock) {
            return maid.getConfigManager().isOpenDoor();
        }
        if (block instanceof FenceGateBlock) {
            return maid.getConfigManager().isOpenFenceGate();
        }
        return true;
    }
}