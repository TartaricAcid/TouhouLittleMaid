package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;

public class MaidPathNavigation extends GroundPathNavigation {
    public MaidPathNavigation(Mob mob, Level level) {
        super(mob, level);
        this.mob.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    @Override
    protected PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new MaidNodeEvaluator();
        this.nodeEvaluator.setCanOpenDoors(true);
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, range);
    }
}