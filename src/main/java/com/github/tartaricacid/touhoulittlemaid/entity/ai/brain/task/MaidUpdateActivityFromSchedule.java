package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.schedule.Activity;

public class MaidUpdateActivityFromSchedule extends Behavior<EntityMaid> {
    public MaidUpdateActivityFromSchedule() {
        super(ImmutableMap.of());
    }

    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        long dayTime = level.getDayTime();
        if (gameTime - brain.lastScheduleUpdate > 20L) {
            Activity activity = brain.getSchedule().getActivityAt((int) (dayTime % 24000L));
            if (!brain.isActive(activity)) {
                maid.getSchedulePos().restrictTo(maid);
                BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
            }
        }
        brain.updateActivityFromSchedule(dayTime, level.getGameTime());
    }
}
