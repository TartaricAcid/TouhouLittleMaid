package com.github.tartaricacid.touhoulittlemaid.entity.ai.path;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Target;

import java.util.Set;

public class MaidUnderWaterBoardingPathFinder extends MaidWrappedPathFinder {
    public MaidUnderWaterBoardingPathFinder(NodeEvaluator pNodeEvaluator, int pMaxVisitedNodes) {
        super(pNodeEvaluator, pMaxVisitedNodes);
    }

    /**
     * 修改了 H 计算方法
     */
    @Override
    public float getBestH(Node node, Set<Target> targets) {
        float maxValue = Float.MAX_VALUE;
        for (Target target : targets) {
            float distance = node.distanceTo(target);
            if (node.y < target.y) {
                distance += (target.y - node.y) * 100;
            }
            target.updateBest(distance, node);
            maxValue = Math.min(maxValue, distance);
        }
        return maxValue;
    }
}
