package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class MaidAwaitTask extends Behavior<EntityMaid> {
    public MaidAwaitTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        Brain<?> brain = maid.getBrain();
        if (maid.isInSittingPose() || maid.isPassenger()) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.setActiveActivityIfPossible(Activity.RIDE);
        }
        if (brain.hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            boolean result = brain.getMemory(MemoryModuleType.WALK_TARGET)
                    .filter(walkTarget -> maid.isWithinRestriction(walkTarget.getTarget().currentBlockPosition()))
                    .isPresent();
            if (!result) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            }
        }
    }
}
