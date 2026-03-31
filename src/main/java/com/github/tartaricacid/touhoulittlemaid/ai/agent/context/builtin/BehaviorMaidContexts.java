package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public final class BehaviorMaidContexts {
    public static final String CATEGORY = "behavior";
    private static final String SUMMARY = "Follow state, sitting, schedule, and current work task.";

    private BehaviorMaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY);
        register.registerContext(CATEGORY, new FollowStateContext());
        register.registerContext(CATEGORY, new SittingContext());
        register.registerContext(CATEGORY, new ScheduleModeContext());
        register.registerContext(CATEGORY, new CurrentActivityContext());
        register.registerContext(CATEGORY, new CurrentTaskContext());
    }

    private static final class FollowStateContext extends AbstractMaidContext {
        private FollowStateContext() {
            super("follow_state", "Follow mode");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.isHomeModeEnable() ? "not following (staying at position)" : "follow (following user)";
        }
    }

    private static final class SittingContext extends AbstractMaidContext {
        private SittingContext() {
            super("sitting", "Sitting state");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.isMaidInSittingPose() ? "sitting" : "standing";
        }
    }

    private static final class ScheduleModeContext extends AbstractMaidContext {
        private ScheduleModeContext() {
            super("schedule_mode", "Work schedule mode");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return switch (maid.getSchedule()) {
                case DAY -> "DAY";
                case NIGHT -> "NIGHT";
                case ALL -> "ALL";
            };
        }
    }

    private static final class CurrentActivityContext extends AbstractMaidContext {
        private CurrentActivityContext() {
            super("current_activity", "Current activity based on schedule");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.getScheduleDetail().getName();
        }
    }

    private static final class CurrentTaskContext extends AbstractMaidContext {
        private CurrentTaskContext() {
            super("current_task", "Current work task");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.getTask().getUid().toString();
        }
    }
}
