package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.google.common.cache.Cache;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BFS版的寻路算法，用于计算从中心开始扩散的若干个点到中心点的可达性
 */
public class MaidPathFindingBFS {
    private final NodeEvaluator nodeEvaluator;
    private final ServerLevel level;
    private BlockPos center;
    private double maxDistance;
    private final Set<BlockPos> cache;
    private boolean isFinished = false;
    private final Queue<Node> queue;
    private Node[] tmp = new Node[20];
    private EntityMaid maid;

    public MaidPathFindingBFS(NodeEvaluator nodeEvaluator, ServerLevel level, EntityMaid maid) {
        this.maid = maid;
        this.nodeEvaluator = nodeEvaluator;
        this.level = level;
        maxDistance = maid.searchRadius();
        int d = (int) Math.ceil(maxDistance);
        center = maid.blockPosition();
        PathNavigationRegion region = new PathNavigationRegion(level,
                center.offset(-d, -d, -d),
                center.offset(d, d, d)
        );
        nodeEvaluator.prepare(region, maid);
        this.cache = new HashSet<>();
        this.cache.add(center);
        this.queue = new LinkedList<>();
        //起点
        Node start = nodeEvaluator.getStart();
        if (start != null)
            this.queue.add(start);
    }

    private boolean canPathReachInternal(BlockPos pos) {
        return this.cache.contains(pos) || this.cache.contains(pos.above()) || this.cache.contains(pos.below());
    }

    public boolean canPathReach(BlockPos pos) {
        if (canPathReachInternal(pos)) return true;
        if (isFinished) return false;
        while (!canPathReachInternal(pos) && !isFinished) {
            searchStep();
        }
        return canPathReachInternal(pos);
    }

    private void searchStep() {
        if (isFinished) return;
        if (queue.isEmpty()) {
            isFinished = true;
            return;
        }
        Node node = queue.poll();
        int neighbors = this.nodeEvaluator.getNeighbors(tmp, node);
        for (int i = 0; i < neighbors; i++) {
//            if (tmp[i].costMalus < 0) continue;
            if (cache.contains(tmp[i].asBlockPos())) continue;
            BlockPos offset = tmp[i].asBlockPos().subtract(center);
            double neighborDistance = offset.getX() * offset.getX() + offset.getZ() * offset.getZ();
            if (neighborDistance > maxDistance * maxDistance) continue;
            cache.add(tmp[i].asBlockPos());
            queue.add(tmp[i]);
        }
    }
}
