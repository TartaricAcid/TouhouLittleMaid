package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidAwaitTask extends Task<EntityMaid> {
    public MaidAwaitTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
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
