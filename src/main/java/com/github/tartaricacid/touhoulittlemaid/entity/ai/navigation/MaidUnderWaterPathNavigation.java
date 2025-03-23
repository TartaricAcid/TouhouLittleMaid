package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.path.MaidSelectivePathFinding;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class MaidUnderWaterPathNavigation extends AmphibiousPathNavigation {
    public MaidUnderWaterPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new MaidUnderWaterNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new MaidSelectivePathFinding(this.nodeEvaluator, maxVisitedNodes, level);
    }
}