package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.tools;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.NONE;

public final class PositionMaidContexts {
    public static final String CATEGORY = "position";
    private static final String SUMMARY = "Self and user positions, distance between them, and light level.";

    private PositionMaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, false);
        register.registerContext(CATEGORY, new MaidPositionContext());
        register.registerContext(CATEGORY, new UserPositionContext());
        register.registerContext(CATEGORY, new DistanceToUserContext());
        register.registerContext(CATEGORY, new LightLevelContext());
    }

    private static final class MaidPositionContext extends AbstractMaidContext {
        private MaidPositionContext() {
            super("self_position", "Self position (x, y, z)");
        }

        @Override
        public String getValue(EntityMaid maid) {
            BlockPos pos = maid.blockPosition();
            return "%d, %d, %d".formatted(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static final class UserPositionContext extends AbstractMaidContext {
        private UserPositionContext() {
            super("user_position", "User position (x, y, z)");
        }

        @Override
        public String getValue(EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner == null) {
                return NONE;
            }
            BlockPos pos = owner.blockPosition();
            return "%d, %d, %d".formatted(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static final class DistanceToUserContext extends AbstractMaidContext {
        private DistanceToUserContext() {
            super("distance_to_user", "Distance to user (blocks)");
        }

        @Override
        public String getValue(EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner == null) {
                return NONE;
            }
            double distance = Math.sqrt(maid.distanceToSqr(owner));
            return "%.1f".formatted(distance);
        }
    }

    private static final class LightLevelContext extends AbstractMaidContext {
        private LightLevelContext() {
            super("light_level", "Light level at self position");
        }

        @Override
        public String getValue(EntityMaid maid) {
            int light = maid.level.getMaxLocalRawBrightness(maid.blockPosition());
            return String.valueOf(light);
        }
    }
}
