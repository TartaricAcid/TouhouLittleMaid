package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.path.MaidSelectivePathFinding;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.path.MaidUnderWaterBoardingPathFinder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class MaidUnderWaterPathNavigation extends AmphibiousPathNavigation {
    public MaidUnderWaterPathNavigation(Mob p_217788_, Level p_217789_) {
        super(p_217788_, p_217789_);
    }

    @Override
    protected PathFinder createPathFinder(int p_217792_) {
        this.nodeEvaluator = new MaidUnderWaterNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new MaidSelectivePathFinding(this.nodeEvaluator, p_217792_, level);
    }
}