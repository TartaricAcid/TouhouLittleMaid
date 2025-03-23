package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation.ICachedEvaluator;
import com.github.tartaricacid.touhoulittlemaid.util.CenterOffsetBlockPosSet;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * BFS版的寻路算法，用于计算从中心开始扩散的若干个点到中心点的可达性
 */
public class MaidPathFindingBFS {
    private final Node[] tmpNode = new Node[20];
    private final CenterOffsetBlockPosSet cachePos;
    private final Queue<Node> queueNode = Lists.newLinkedList();
    private final NodeEvaluator nodeEvaluator;
    private final BlockPos centerPos;
    private final double maxDistance;
    private final int verticalSearchRange;

    private boolean isFinished = false;

    public MaidPathFindingBFS(NodeEvaluator nodeEvaluator, ServerLevel level, EntityMaid maid) {
        this(nodeEvaluator, level, maid, maid.searchRadius(), 7);
    }

    public MaidPathFindingBFS(NodeEvaluator nodeEvaluator, ServerLevel level, EntityMaid maid, int verticalSearchRange) {
        this(nodeEvaluator, level, maid, maid.searchRadius(), verticalSearchRange);
    }

    @SuppressWarnings("all")
    public MaidPathFindingBFS(NodeEvaluator nodeEvaluator, ServerLevel level, EntityMaid maid, float maxDistance, int verticalSearchRange) {
        this.nodeEvaluator = nodeEvaluator;
        this.centerPos = maid.blockPosition();
        this.maxDistance = maxDistance;
        this.verticalSearchRange = verticalSearchRange;

        int offset = (int) Math.ceil(this.maxDistance);
        PathNavigationRegion region = new PathNavigationRegion(level,
                centerPos.offset(-offset, -verticalSearchRange, -offset),
                centerPos.offset(offset, verticalSearchRange, offset));
        this.cachePos = new CenterOffsetBlockPosSet(
                offset + 1, verticalSearchRange + 1, offset + 1,
                centerPos.getX(), centerPos.getY(), centerPos.getZ()
        );
        if (nodeEvaluator instanceof ICachedEvaluator ice) {
            ice.init(offset, verticalSearchRange, offset, centerPos.getX(), centerPos.getY(), centerPos.getZ());
        }
        nodeEvaluator.prepare(region, maid);
        Node start = nodeEvaluator.getStart();
        if (start != null) {
            this.cachePos.markVis(start.asBlockPos());
            this.queueNode.add(start);
        }
    }

    private boolean canPathReachInternal(BlockPos pos) {
        return this.cachePos.isVis(pos) || this.cachePos.isVis(pos.above());
    }

    public boolean canPathReach(BlockPos pos) {
        if (canPathReachInternal(pos)) {
            return true;
        }
        if (isFinished) {
            return false;
        }
        while (!canPathReachInternal(pos) && !isFinished) {
            searchStep();
        }
        return canPathReachInternal(pos);
    }

    @Nullable
    private BlockPos searchStep() {
        if (isFinished) {
            return null;
        }
        if (queueNode.isEmpty()) {
            isFinished = true;
            return null;
        }
        Node node = queueNode.poll();
        int neighbors = this.nodeEvaluator.getNeighbors(tmpNode, node);
        for (int i = 0; i < neighbors; i++) {
            if (cachePos.isVis(tmpNode[i].asBlockPos())) {
                continue;
            }
            BlockPos offset = tmpNode[i].asBlockPos().subtract(centerPos);
            if (verticalSearchRange < offset.getY() || offset.getY() < -verticalSearchRange) {
                continue;
            }
            double neighborDistance = offset.getX() * offset.getX() + offset.getZ() * offset.getZ();
            if (neighborDistance > maxDistance * maxDistance) {
                continue;
            }
            cachePos.markVis(tmpNode[i].asBlockPos());
            if (this.nodeEvaluator instanceof ICachedEvaluator ice) {
                ice.markVis(tmpNode[i].asBlockPos());
            }
            queueNode.add(tmpNode[i]);
        }
        return node.asBlockPos();
    }

    public Optional<BlockPos> find(Predicate<BlockPos> predicate) {
        while (!isFinished) {
            BlockPos blockPos = searchStep();
            if (blockPos != null && predicate.test(blockPos)) {
                return Optional.of(blockPos);
            }
        }
        return Optional.empty();
    }

    public void finish() {
        this.isFinished = true;
        this.nodeEvaluator.done();
    }
}
