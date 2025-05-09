package com.github.tartaricacid.touhoulittlemaid.entity.ai.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 水下的时候让女仆选择合适的路径计算器，使用不同的启发函数进行寻路
 * 如果目标是上岸，则对 Y 差值给出额外的开销，来减少向下搜索的数量
 */
public class MaidSelectivePathFinding extends MaidWrappedPathFinder {
    private final MaidUnderWaterBoardingPathFinder boarding;
    private final BlockGetter level;

    public MaidSelectivePathFinding(NodeEvaluator nodeEvaluator, int maxVisitedNodes, BlockGetter level) {
        super(nodeEvaluator, maxVisitedNodes);
        this.level = level;
        this.boarding = new MaidUnderWaterBoardingPathFinder(nodeEvaluator, maxVisitedNodes);
    }

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion region, Mob mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        if (targetPositions.stream().anyMatch(p -> level.getFluidState(p).isEmpty())) {
            return boarding.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
        }
        return super.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
    }
}
