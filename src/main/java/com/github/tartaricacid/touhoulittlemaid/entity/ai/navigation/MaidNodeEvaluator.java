package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * 该方法仅修改了栅栏门的寻路判断
 */
public class MaidNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int pX, int pY, int pZ) {
        return getMaidBlockPathTypeStatic(level, new BlockPos.MutableBlockPos(pX, pY, pZ));
    }

    @Override
    public int getNeighbors(Node[] pOutputArray, Node pNode) {
        return this.createClimbNode(super.getNeighbors(pOutputArray, pNode), pOutputArray, pNode);
    }

    // 将可爬行物加入寻路节点里头
    // 一般这些物体都是相连的，所以向上向下搜寻下
    protected int createClimbNode(int nodeID, Node[] nodes, Node origin) {
        // 如果禁用主动攀爬能力，就直接返回，不把可攀爬物体加入寻路节点中
        if (this.mob instanceof EntityMaid maid && !maid.getConfigManager().isActiveClimbing()) {
            return nodeID;
        }

        Level level = this.mob.level;

        // 向上搜寻
        BlockPos.MutableBlockPos upPos = new BlockPos.MutableBlockPos(origin.x, origin.y + 1, origin.z);
        if (level.getBlockState(upPos).isLadder(level, upPos, this.mob)) {
            Node node = this.getNode(upPos);
            if (!node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (nodeID + 1 < nodes.length)
                    nodes[nodeID++] = node;
            }
        }
        // 向下搜寻
        BlockPos.MutableBlockPos downPos = new BlockPos.MutableBlockPos(origin.x, origin.y - 1, origin.z);
        if (level.getBlockState(downPos).isLadder(level, downPos, this.mob)) {
            Node node = this.getNode(downPos);
            if (!node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (nodeID + 1 < nodes.length)
                    nodes[nodeID++] = node;
            }
        }
        return nodeID;
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
        } else if (this.mob instanceof EntityMaid maid && this.canClimb(blockState, pos, maid)) { //将楼梯视为可行走方块，便于后续将楼梯加入路径节点
            pathType = BlockPathTypes.WALKABLE;
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

    private boolean canClimb(BlockState blockState, BlockPos blockPos, EntityMaid maid) {
        // 暂时禁用脚手架的攀爬能力，
        // 脚手架太奇怪了，上爬没问题，但是下爬时不知有啥东西在阻碍着，导致不能向下爬
        if (isMaidCanClimbBlock(blockState, blockPos, maid)) {
            return maid.getConfigManager().isActiveClimbing();
        }
        return false;
    }

    public static boolean isMaidCanClimbBlock(BlockState blockState, BlockPos blockPos, EntityMaid maid) {
        return blockState.isLadder(maid.level, blockPos, maid) && !blockState.isScaffolding(maid);
    }
}