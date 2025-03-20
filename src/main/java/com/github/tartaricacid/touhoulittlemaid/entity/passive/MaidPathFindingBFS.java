package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;

import java.util.Queue;
import java.util.Set;

/**
 * BFS版的寻路算法，用于计算从中心开始扩散的若干个点到中心点的可达性
 */
public class MaidPathFindingBFS {
    private final Node[] tmpNode = new Node[20];
    private final Set<BlockPos> cachePos = Sets.newHashSet();
    private final Queue<Node> queueNode = Lists.newLinkedList();
    private final NodeEvaluator nodeEvaluator;
    private final BlockPos centerPos;
    private final double maxDistance;

    private boolean isFinished = false;

    @SuppressWarnings("all")
    public MaidPathFindingBFS(NodeEvaluator nodeEvaluator, ServerLevel level, EntityMaid maid) {
        this.nodeEvaluator = nodeEvaluator;
        this.centerPos = maid.blockPosition();
        this.maxDistance = maid.searchRadius();
        this.cachePos.add(this.centerPos);

        int offset = (int) Math.ceil(this.maxDistance);
        PathNavigationRegion region = new PathNavigationRegion(level,
                centerPos.offset(-offset, -offset, -offset),
                centerPos.offset(offset, offset, offset));
        nodeEvaluator.prepare(region, maid);
        Node start = nodeEvaluator.getStart();
        if (start != null) {
            this.queueNode.add(start);
        }
    }

    private boolean canPathReachInternal(BlockPos pos) {
        return this.cachePos.contains(pos) || this.cachePos.contains(pos.above()) || this.cachePos.contains(pos.below());
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

    private void searchStep() {
        if (isFinished) {
            return;
        }
        if (queueNode.isEmpty()) {
            isFinished = true;
            return;
        }
        Node node = queueNode.poll();
        int neighbors = this.nodeEvaluator.getNeighbors(tmpNode, node);
        for (int i = 0; i < neighbors; i++) {
            if (cachePos.contains(tmpNode[i].asBlockPos())) {
                continue;
            }
            BlockPos offset = tmpNode[i].asBlockPos().subtract(centerPos);
            double neighborDistance = offset.getX() * offset.getX() + offset.getZ() * offset.getZ();
            if (neighborDistance > maxDistance * maxDistance) {
                continue;
            }
            cachePos.add(tmpNode[i].asBlockPos());
            queueNode.add(tmpNode[i]);
        }
    }
}
