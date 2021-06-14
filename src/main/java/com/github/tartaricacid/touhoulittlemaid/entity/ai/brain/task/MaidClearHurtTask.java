package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidClearHurtTask extends Task<EntityMaid> {
    public MaidClearHurtTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        boolean hurtOrHostile = MaidPanicTask.isHurt(maid) || MaidPanicTask.hasHostile(maid);
        if (MaidPanicTask.isAttack(maid) || !hurtOrHostile) {
            maid.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
            maid.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            maid.getBrain().updateActivityFromSchedule(worldIn.getDayTime(), worldIn.getGameTime());
        }
    }
}
