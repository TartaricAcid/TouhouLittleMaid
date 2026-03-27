package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.NONE;

public final class PositionMaidContexts {
    public static final String CATEGORY = "position";
    private static final String SUMMARY = "Maid and owner positions, distance between them, and light level.";

    private PositionMaidContexts() {
    }

    public static void registerAll(MaidContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY);
        register.registerContext(CATEGORY, new MaidPositionContext());
        register.registerContext(CATEGORY, new OwnerPositionContext());
        register.registerContext(CATEGORY, new DistanceToOwnerContext());
        register.registerContext(CATEGORY, new LightLevelContext());
    }

    private static final class MaidPositionContext extends AbstractMaidContext {
        private MaidPositionContext() {
            super("maid_position", "Maid position (x, y, z)");
        }

        @Override
        public String getValue(EntityMaid maid) {
            BlockPos pos = maid.blockPosition();
            return "%d, %d, %d".formatted(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static final class OwnerPositionContext extends AbstractMaidContext {
        private OwnerPositionContext() {
            super("owner_position", "Owner position (x, y, z)");
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

    private static final class DistanceToOwnerContext extends AbstractMaidContext {
        private DistanceToOwnerContext() {
            super("distance_to_owner", "Distance to owner (blocks)");
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
            super("light_level", "Light level at maid position");
        }

        @Override
        public String getValue(EntityMaid maid) {
            int light = maid.level.getMaxLocalRawBrightness(maid.blockPosition());
            return String.valueOf(light);
        }
    }
}
