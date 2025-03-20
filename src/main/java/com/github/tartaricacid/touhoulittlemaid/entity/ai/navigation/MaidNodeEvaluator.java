package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 该方法仅修改了栅栏门和梯子的寻路判断
 */
public class MaidNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int pX, int pY, int pZ) {
        return getMaidBlockPathTypeStatic(level, new BlockPos.MutableBlockPos(pX, pY, pZ));
    }

    @Override
    public int getNeighbors(Node[] outputArray, Node node) {
        int nodeId = super.getNeighbors(outputArray, node);
        return this.createClimbNode(nodeId, outputArray, node);
    }

    // 将可爬行物加入寻路节点里头
    // 一般这些物体都是相连的，所以向上向下搜寻下
    protected int createClimbNode(int nodeId, Node[] nodes, Node origin) {
        // 只有在开启攀爬能力，才将梯子加入寻路节点里
        if (this.mob instanceof EntityMaid maid && maid.getConfigManager().isActiveClimbing()) {
            // 向上搜寻
            BlockPos.MutableBlockPos upPos = new BlockPos.MutableBlockPos(origin.x, origin.y + 1, origin.z);
            if (isMaidCanClimbBlock(upPos, maid)) {
                Node node = this.getNode(upPos);
                if (!node.closed) {
                    node.costMalus = 0;
                    node.type = BlockPathTypes.WALKABLE;
                    if (nodeId + 1 < nodes.length) {
                        nodes[nodeId++] = node;
                    }
                }
            }
            // 向下搜寻
            BlockPos.MutableBlockPos downPos = new BlockPos.MutableBlockPos(origin.x, origin.y - 1, origin.z);
            if (isMaidCanClimbBlock(downPos, maid)) {
                Node node = this.getNode(downPos);
                if (!node.closed) {
                    node.costMalus = 0;
                    node.type = BlockPathTypes.WALKABLE;
                    if (nodeId + 1 < nodes.length) {
                        nodes[nodeId++] = node;
                    }
                }
            }
        }
        return nodeId;
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
        // 女仆在限定范围内寻路寻到了范围外，失败
        if (this.mob instanceof EntityMaid maid && maid.isWithinRestriction() && !maid.isWithinRestriction(pos)) {
            return BlockPathTypes.BLOCKED;
        }
        BlockState blockState = level.getBlockState(pos);
        BlockPathTypes pathType = blockState.getBlockPathType(level, pos, null);
        if (pathType != null) {
            return pathType;
        } else if (blockState.isAir()) {
            return BlockPathTypes.OPEN;
        } else if (blockState.getBlock() instanceof FenceGateBlock) {
            pathType = blockState.getValue(FenceGateBlock.OPEN) ? BlockPathTypes.DOOR_OPEN : BlockPathTypes.DOOR_WOOD_CLOSED;
        } else if (this.mob instanceof EntityMaid maid && this.canClimb(blockState, pos, maid)) {
            // 将楼梯视为可行走方块，便于后续将楼梯加入路径节点
            pathType = BlockPathTypes.WALKABLE;
        } else {
            pathType = WalkNodeEvaluator.getBlockPathTypeRaw(level, pos);
            // 判断目标方块的碰撞高度。有些半透明方块拥有超过 0.5（台阶）的高度，此时女仆是不能从其中穿过的，需要将其视为不可通行方块
            VoxelShape shape = blockState.getCollisionShape(level, pos);
            if (pathType != BlockPathTypes.BLOCKED && shape.max(Direction.Axis.Y) - shape.min(Direction.Axis.Y) > 0.5) {
                pathType = BlockPathTypes.BLOCKED;
            }
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
        if (isMaidCanClimbBlock(blockState, blockPos, maid)) {
            return maid.getConfigManager().isActiveClimbing();
        }
        return false;
    }

    public static boolean isMaidCanClimbBlock(BlockPos blockPos, EntityMaid maid) {
        Level level = maid.level;
        BlockState blockState = level.getBlockState(blockPos);
        return isMaidCanClimbBlock(blockState, blockPos, maid);
    }

    public static boolean isMaidCanClimbBlock(BlockState blockState, BlockPos blockPos, EntityMaid maid) {
        return blockState.isLadder(maid.level, blockPos, maid);
    }
}