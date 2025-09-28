package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * 该方法仅修改了栅栏门和梯子的寻路判断
 */
public class MaidNodeEvaluator extends WalkNodeEvaluator implements INodeCacheEvaluator {
    boolean noValidation = false;

    @Override
    public void prepare(PathNavigationRegion level, Mob mob) {
        super.prepare(level, mob);
        // 这里尝试从其他女仆获取相邻点缓存
        if (mob instanceof EntityMaid maid && maid.level() instanceof ServerLevel sl) {
            BlockPos pos = maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition();
            int restrictRadius = maid.hasRestriction() ? (int) maid.getRestrictRadius() : 5;
            int tickCount = sl.getServer().getTickCount();
            // 如果女仆存在这一刻的缓存，那么直接可以使用
            if (maid.nodeNeighborCache != null && maid.nodeNeighborCache.tickCount == tickCount) {
                return;
            }
            // 否则，寻找离自己最近的女仆
            Optional<EntityMaid> min = mob.level().getEntities(EntityTypeTest.forClass(EntityMaid.class),
                    new AABB(pos).inflate(restrictRadius),
                    m -> m.nodeNeighborCache != null && m.nodeNeighborCache.tickCount == tickCount
            ).stream().min((t, t2) -> (int) (t.distanceTo(maid) - t2.distanceTo(maid)));
            // 将其缓存复制到自己
            // 否则创建新的缓存
            maid.nodeNeighborCache = min.map(entityMaid -> NodeNeighborCache.copyToAnotherCenter(
                            entityMaid.nodeNeighborCache, pos.getX(), pos.getY(), pos.getZ()))
                    .orElseGet(() -> new NodeNeighborCache(pos.getX(), pos.getY(), pos.getZ(),
                            restrictRadius, 10, restrictRadius, tickCount));
        }
    }

    @Override
    public PathType getPathType(PathfindingContext pContext, int pX, int pY, int pZ) {
        return getMaidBlockPathTypeStatic(pContext, new BlockPos.MutableBlockPos(pX, pY, pZ));
    }

    @Override
    public int getNeighbors(Node[] outputArray, Node node) {
        //优先尝试从缓存中获取相邻点
        int nodeId = -1;
        if (mob instanceof EntityMaid maid) {
            nodeId = maid.nodeNeighborCache.get(node, outputArray, this);
        }
        //没有获取到，那么重新进行计算
        if (nodeId == -1) {
            //首先，不判断节点合法性（因为其与上下文相关）
            noValidation = true;
            //获取相邻点
            nodeId = super.getNeighbors(outputArray, node);
            noValidation = false;
            if (mob instanceof EntityMaid maid) {
                //缓存相邻点
                maid.nodeNeighborCache.record(node, outputArray, nodeId);
            }
        }
        //然后，删除不合法点
        int offset = 0;
        for (int i = 0; i < nodeId; i++) {
            if (!isNeighborValid(outputArray[i], node)) {
                offset++;
            } else if (offset != 0) {
                outputArray[i - offset] = outputArray[i];
            }
        }
        if (offset != 0) {
            nodeId -= offset;
        }
        return this.createClimbNode(nodeId, outputArray, node);
    }

    @Override
    protected boolean isNeighborValid(@Nullable Node neighbor, Node node) {
        if (neighbor != null && noValidation) {
            return true;
        }
        return super.isNeighborValid(neighbor, node);
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
                    node.type = PathType.WALKABLE;
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
                    node.type = PathType.WALKABLE;
                    if (nodeId + 1 < nodes.length) {
                        nodes[nodeId++] = node;
                    }
                }
            }
        }
        return nodeId;
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
        // 女仆在限定范围内寻路寻到了范围外，失败
        if (this.mob instanceof EntityMaid maid && maid.isWithinRestriction() && !maid.isWithinRestriction(pos)) {
            return PathType.BLOCKED;
        }
        BlockState blockState = context.getBlockState(pos);
        PathType pathType;
        if (blockState.getBlock() instanceof FenceGateBlock) {
            pathType = blockState.getValue(FenceGateBlock.OPEN) ? PathType.DOOR_OPEN : PathType.DOOR_WOOD_CLOSED;
        } else if (this.mob instanceof EntityMaid maid && this.canClimb(blockState, pos, maid)) {
            // 将楼梯视为可行走方块，便于后续将楼梯加入路径节点
            pathType = PathType.WALKABLE;
        } else {
            pathType = context.getPathTypeFromState(pX, pY, pZ);
            // 判断目标方块的碰撞高度。有些半透明方块拥有超过 0.5（台阶）的高度，此时女仆是不能从其中穿过的，需要将其视为不可通行方块
            if (!heightCheckExclusions(pathType) && this.mob != null) {
                VoxelShape shape = blockState.getCollisionShape(this.mob.level, pos);
                if (pathType != PathType.BLOCKED && shape.max(Direction.Axis.Y) - shape.min(Direction.Axis.Y) > 0.5) {
                    pathType = PathType.BLOCKED;
                }
            }
        }
        if (pathType == PathType.DOOR_WOOD_CLOSED && this.mob instanceof EntityMaid maid && !this.canOpenDoor(blockState.getBlock(), maid)) {
            pathType = PathType.DOOR_IRON_CLOSED;
        }
        return pathType;
    }

    private boolean heightCheckExclusions(PathType pathType) {
        return pathType == PathType.DOOR_OPEN || pathType == PathType.DOOR_WOOD_CLOSED;
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

    @Override
    public Node createNode(int x, int y, int z) {
        return getNode(x, y, z);
    }
}