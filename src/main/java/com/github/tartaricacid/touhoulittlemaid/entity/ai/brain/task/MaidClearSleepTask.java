package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidClearSleepTask extends Task<EntityMaid> {
    public MaidClearSleepTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        if (entityIn.isSleeping() && !entityIn.getBrain().isActive(Activity.REST)) {
            entityIn.stopSleeping();
        }
    }
}
