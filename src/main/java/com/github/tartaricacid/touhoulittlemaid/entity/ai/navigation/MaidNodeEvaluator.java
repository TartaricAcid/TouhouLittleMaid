package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * 该方法仅修改了栅栏门的寻路判断
 */
public class MaidNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int pX, int pY, int pZ) {
        return getMaidBlockPathTypeStatic(level, new BlockPos.MutableBlockPos(pX, pY, pZ));
    }

    private BlockPathTypes getMaidBlockPathTypeStatic(BlockGetter level, BlockPos.MutableBlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        BlockPathTypes type = getMaidBlockPathTypeRaw(level, pos);
        if (type == BlockPathTypes.OPEN && y >= level.getMinBuildHeight() + 1) {
            BlockPathTypes typeBelow = getMaidBlockPathTypeRaw(level, pos.set(x, y - 1, z));

            type = typeBelow != BlockPathTypes.WALKABLE
                   && typeBelow != BlockPathTypes.OPEN
                   && typeBelow != BlockPathTypes.WATER
                   && typeBelow != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;

            if (typeBelow == BlockPathTypes.DAMAGE_FIRE) {
                type = BlockPathTypes.DAMAGE_FIRE;
            }

            if (typeBelow == BlockPathTypes.DAMAGE_OTHER) {
                type = BlockPathTypes.DAMAGE_OTHER;
            }

            if (typeBelow == BlockPathTypes.STICKY_HONEY) {
                type = BlockPathTypes.STICKY_HONEY;
            }

            if (typeBelow == BlockPathTypes.POWDER_SNOW) {
                type = BlockPathTypes.DANGER_POWDER_SNOW;
            }

            if (typeBelow == BlockPathTypes.DAMAGE_CAUTIOUS) {
                type = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }

        if (type == BlockPathTypes.WALKABLE) {
            type = checkNeighbourBlocks(level, pos.set(x, y, z), type);
        }

        return type;
    }

    private BlockPathTypes getMaidBlockPathTypeRaw(BlockGetter level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        BlockPathTypes pathType = blockState.getBlockPathType(level, pos, null);
        if (pathType != null) {
            return pathType;
        } else if (blockState.isAir()) {
            return BlockPathTypes.OPEN;
        } else if (blockState.getBlock() instanceof FenceGateBlock) {
            pathType = blockState.getValue(FenceGateBlock.OPEN) ? BlockPathTypes.DOOR_OPEN : BlockPathTypes.DOOR_WOOD_CLOSED;
        } else {
            pathType = WalkNodeEvaluator.getBlockPathTypeRaw(level, pos);
        }
        if (pathType == BlockPathTypes.DOOR_WOOD_CLOSED && this.mob instanceof EntityMaid maid && !this.canOpenDoor(blockState.getBlock(), maid)) {
            pathType = BlockPathTypes.DOOR_IRON_CLOSED;
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