package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;

public class MaidUnderWaterPathNavigation extends AmphibiousPathNavigation {
    public MaidUnderWaterPathNavigation(Mob p_217788_, Level p_217789_) {
        super(p_217788_, p_217789_);
    }
    @Override
    protected PathFinder createPathFinder(int p_217792_) {
        this.nodeEvaluator = new MaidUnderWaterNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, p_217792_);
    }
}