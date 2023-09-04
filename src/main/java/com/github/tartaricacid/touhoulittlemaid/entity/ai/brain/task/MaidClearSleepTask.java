package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.schedule.Activity;

public class MaidClearSleepTask extends Behavior<EntityMaid> {
    public MaidClearSleepTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        if (entityIn.isSleeping() && !entityIn.getBrain().isActive(Activity.REST)) {
            entityIn.stopSleeping();
        }
    }
}
