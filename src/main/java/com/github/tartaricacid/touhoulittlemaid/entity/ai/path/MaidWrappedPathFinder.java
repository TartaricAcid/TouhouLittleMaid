package com.github.tartaricacid.touhoulittlemaid.entity.ai.path;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugMaidManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于加入debug信息
 */
public class MaidWrappedPathFinder extends PathFinder {
    protected Mob mob;

    public MaidWrappedPathFinder(NodeEvaluator nodeEvaluator, int maxVisitedNodes) {
        super(nodeEvaluator, maxVisitedNodes);
    }

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion region, Mob mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        this.mob = mob;
        return super.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
    }

    @Nullable
    @Override
    public Path findPath(ProfilerFiller profiler, Node node, Map<Target, BlockPos> targetPos, float maxRange, int accuracy, float searchDepthMultiplier) {
        if (TouhouLittleMaid.DEBUG && mob instanceof EntityMaid maid && DebugMaidManager.getDebuggingPlayer(maid) != null) {
            List<Node> tmpClosedSet = Lists.newArrayList();
            this.openSet = new BinaryHeap() {
                @Override
                public @NotNull Node pop() {
                    Node pop = super.pop();
                    tmpClosedSet.add(pop);
                    return pop;
                }
            };
            Path path = super.findPath(profiler, node, targetPos, maxRange, accuracy, searchDepthMultiplier);
            if (path == null) {
                return null;
            }
            // 因为上面 wrap 了一手 pop，所以下面的代码会导致 close 的大小发生变化，需要先处理
            Node[] closedSet = tmpClosedSet.toArray(new Node[0]);

            Node[] openSet = new Node[this.openSet.size()];
            int idx = 0;
            while (!this.openSet.isEmpty()) {
                openSet[idx++] = this.openSet.pop();
            }
            path.setDebug(openSet, closedSet, targetPos.keySet());
            return path;
        }
        return super.findPath(profiler, node, targetPos, maxRange, accuracy, searchDepthMultiplier);
    }
}
