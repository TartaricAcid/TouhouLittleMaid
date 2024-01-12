package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidUpdateActivityFromSchedule extends Task<EntityMaid> {
    private Activity cacheActivity;

    public MaidUpdateActivityFromSchedule() {
        super(ImmutableMap.of());
    }

    protected void start(ServerWorld level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        long dayTime = level.getDayTime();
        if (gameTime - brain.lastScheduleUpdate > 20L) {
            Activity activity = brain.getSchedule().getActivityAt((int) (dayTime % 24000L));
            if (this.cacheActivity == null) {
                this.cacheActivity = activity;
            }
            if (!this.cacheActivity.equals(activity) && this.maidStateConditions(maid)) {
                this.cacheActivity = activity;
                maid.getSchedulePos().restrictTo(maid);
                BrainUtil.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
            }
        }
        brain.updateActivityFromSchedule(dayTime, level.getGameTime());
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return maid.isHomeModeEnable() && !maid.isInSittingPose() && !maid.isSleeping() && !maid.isLeashed() && !maid.isPassenger();
    }
}