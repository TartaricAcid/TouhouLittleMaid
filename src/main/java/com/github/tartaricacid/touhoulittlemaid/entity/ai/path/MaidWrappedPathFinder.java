package com.github.tartaricacid.touhoulittlemaid.entity.ai.path;

import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugMaidManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 用于加入debug信息
 */
public class MaidWrappedPathFinder extends PathFinder {
    public MaidWrappedPathFinder(NodeEvaluator pNodeEvaluator, int pMaxVisitedNodes) {
        super(pNodeEvaluator, pMaxVisitedNodes);
    }

    Mob pMob;

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion pRegion, Mob pMob, Set<BlockPos> pTargetPositions, float pMaxRange, int pAccuracy, float pSearchDepthMultiplier) {
        this.pMob = pMob;
        return super.findPath(pRegion, pMob, pTargetPositions, pMaxRange, pAccuracy, pSearchDepthMultiplier);
    }

    @Nullable
    @Override
    public Path findPath(ProfilerFiller pProfiler, Node pNode, Map<Target, BlockPos> pTargetPos, float pMaxRange, int pAccuracy, float pSearchDepthMultiplier) {
        if (pMob instanceof EntityMaid maid && DebugMaidManager.getDebuggingPlayer(maid) != null) {
            List<Node> tmpClosedSet = new ArrayList<>();
            this.openSet = new BinaryHeap() {
                @Override
                public @NotNull Node pop() {
                    Node pop = super.pop();
                    tmpClosedSet.add(pop);
                    return pop;
                }
            };
            Path path = super.findPath(pProfiler, pNode, pTargetPos, pMaxRange, pAccuracy, pSearchDepthMultiplier);
            if (path == null) {
                return null;
            }
            //因为上面wrap了一手pop，所以下面的代码会导致close的大小发生变化，需要先处理
            Node[] closedSet = new Node[tmpClosedSet.size()];
            for (int i = 0; i < tmpClosedSet.size(); i++) {
                closedSet[i] = tmpClosedSet.get(i);
            }

            Node[] openSet = new Node[this.openSet.size()];
            int idx = 0;
            while (!this.openSet.isEmpty()) {
                openSet[idx++] = this.openSet.pop();
            }
            path.setDebug(openSet, closedSet, pTargetPos.keySet());
            return path;
        }
        return super.findPath(pProfiler, pNode, pTargetPos, pMaxRange, pAccuracy, pSearchDepthMultiplier);
    }
}
