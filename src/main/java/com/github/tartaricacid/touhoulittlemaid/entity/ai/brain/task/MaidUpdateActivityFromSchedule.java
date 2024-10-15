package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class MaidUpdateActivityFromSchedule extends Behavior<EntityMaid> {
    private Activity cacheActivity;

    public MaidUpdateActivityFromSchedule() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        long dayTime = level.getDayTime();

        // 让女仆在切换日程表时能够改变自己的活动范围
        if (gameTime - brain.lastScheduleUpdate > 20L) {
            Activity activity = brain.getSchedule().getActivityAt((int) (dayTime % 24000L));
            if (this.cacheActivity == null) {
                this.cacheActivity = activity;
            }
            if (!this.cacheActivity.equals(activity) && maid.isHomeModeEnable() && maid.canBrainMoving()) {
                this.cacheActivity = activity;
                maid.getSchedulePos().restrictTo(maid);
                BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
            }
        }

        // 切换日程表，分骑乘和非骑乘两种情况
        updateActivityFromSchedule(level, maid, brain, gameTime);
    }

    public static void updateActivityFromSchedule(EntityMaid maid, Brain<EntityMaid> brain) {
        if (maid.level instanceof ServerLevel serverLevel) {
            long gameTime = serverLevel.getGameTime();
            updateActivityFromSchedule(serverLevel, maid, brain, gameTime);
        }
    }

    public static void updateActivityFromSchedule(EntityMaid maid) {
        if (maid.level instanceof ServerLevel serverLevel) {
            long gameTime = serverLevel.getGameTime();
            Brain<EntityMaid> brain = maid.getBrain();
            updateActivityFromSchedule(serverLevel, maid, brain, gameTime);
        }
    }

    private static void updateActivityFromSchedule(ServerLevel level, EntityMaid maid, Brain<EntityMaid> brain, long gameTime) {
        long dayTime = level.getDayTime();
        if (maid.isMaidInSittingPose() || maid.isPassenger()) {
            if (gameTime - brain.lastScheduleUpdate > 20L) {
                brain.lastScheduleUpdate = gameTime;
                Activity activity = brain.getSchedule().getActivityAt((int) (dayTime % 24000L));
                Activity riderActivity;
                if (activity.equals(Activity.WORK)) {
                    riderActivity = InitEntities.RIDE_WORK.get();
                } else if (activity.equals(Activity.IDLE)) {
                    riderActivity = InitEntities.RIDE_IDLE.get();
                } else {
                    riderActivity = InitEntities.RIDE_REST.get();
                }
                if (!brain.isActive(riderActivity)) {
                    brain.eraseMemory(MemoryModuleType.PATH);
                    brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                    brain.setActiveActivityIfPossible(riderActivity);
                }
            }
        } else {
            brain.updateActivityFromSchedule(dayTime, level.getGameTime());
        }
    }
}
