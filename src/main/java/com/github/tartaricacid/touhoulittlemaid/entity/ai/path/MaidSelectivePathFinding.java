package com.github.tartaricacid.touhoulittlemaid.entity.ai.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 水下的时候让女仆选择合适的路径计算器，使用不同的启发函数进行寻路
 * 如果目标是登录，则对Y差值给出额外的开销，来减少向下搜索的数量
 */
public class MaidSelectivePathFinding extends PathFinder {
    private final MaidUnderWaterBoardingPathFinder boarding;
    private final BlockGetter level;

    public MaidSelectivePathFinding(NodeEvaluator pNodeEvaluator, int pMaxVisitedNodes, BlockGetter pLevel) {
        super(pNodeEvaluator, pMaxVisitedNodes);
        this.level = pLevel;
        this.boarding = new MaidUnderWaterBoardingPathFinder(pNodeEvaluator, pMaxVisitedNodes);
    }

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion pRegion, Mob pMob, Set<BlockPos> pTargetPositions, float pMaxRange, int pAccuracy, float pSearchDepthMultiplier) {
        if (pTargetPositions.stream().anyMatch(p -> level.getFluidState(p).isEmpty())) {
            return boarding.findPath(pRegion, pMob, pTargetPositions, pMaxRange, pAccuracy, pSearchDepthMultiplier);
        }
        return super.findPath(pRegion, pMob, pTargetPositions, pMaxRange, pAccuracy, pSearchDepthMultiplier);
    }
}
