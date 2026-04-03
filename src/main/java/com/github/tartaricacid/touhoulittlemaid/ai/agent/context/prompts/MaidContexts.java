package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.prompts;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.HEALTHY_FORMAT;

public final class MaidContexts {
    public static final String CATEGORY = "status";
    private static final String SUMMARY = "Self some status";

    private MaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, true);
        register.registerContext(CATEGORY, new MaidHealthContext());
        register.registerContext(CATEGORY, new MaidIsSleepContext());
        register.registerContext(CATEGORY, new FollowStateContext());
        register.registerContext(CATEGORY, new SittingContext());
        register.registerContext(CATEGORY, new RideContext());
        register.registerContext(CATEGORY, new ScheduleModeContext());
        register.registerContext(CATEGORY, new CurrentActivityContext());
        register.registerContext(CATEGORY, new CurrentTaskContext());
    }

    private static final class MaidHealthContext extends AbstractMaidContext {
        private MaidHealthContext() {
            super("healthy", "Self health");
        }

        @Override
        public String getValue(EntityMaid maid) {
            float maxHealth = maid.getMaxHealth();
            float health = maid.getHealth();
            return HEALTHY_FORMAT.formatted(health, maxHealth);
        }
    }

    private static final class MaidIsSleepContext extends AbstractMaidContext {
        private MaidIsSleepContext() {
            super("sleep_state", "Is sleeping");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.isSleeping() ? "yes" : "no";
        }
    }

    private static final class FollowStateContext extends AbstractMaidContext {
        private FollowStateContext() {
            super("follow_state", "Is following");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.isHomeModeEnable() ? "no" : "yes";
        }
    }

    private static final class SittingContext extends AbstractMaidContext {
        private SittingContext() {
            super("sitting", "Sitting");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.isMaidInSittingPose() ? "yes" : "no";
        }
    }

    private static final class RideContext extends AbstractMaidContext {
        private RideContext() {
            super("riding", "Is riding");
        }

        @Override
        public String getValue(EntityMaid maid) {
            Entity vehicle = maid.getVehicle();
            if (vehicle == null) {
                return "not";
            }
            ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(vehicle.getType());
            if (type == null) {
                return "not";
            }
            return "riding %s".formatted(type);
        }
    }

    private static final class ScheduleModeContext extends AbstractMaidContext {
        private ScheduleModeContext() {
            super("schedule", "Schedule");
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
            super("activity", "Activity");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.getScheduleDetail().getName();
        }
    }

    private static final class CurrentTaskContext extends AbstractMaidContext {
        private CurrentTaskContext() {
            super("work_task", "Work task");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return maid.getTask().getUid().toString();
        }
    }
}
