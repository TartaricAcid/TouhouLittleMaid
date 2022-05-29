package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class MaidRunAwayTask<T> extends SetWalkTargetAwayFrom<T> {
    public MaidRunAwayTask(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag, Function<T, Vec3> toPosition) {
        super(walkAwayFromMemory, speedModifier, desiredDistance, flag, toPosition);
    }

    public static MaidRunAwayTask<BlockPos> pos(MemoryModuleType<BlockPos> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, flag, Vec3::atBottomCenterOf);
    }

    public static MaidRunAwayTask<? extends Entity> entity(MemoryModuleType<? extends Entity> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, flag, Entity::position);
    }

    @Override
    protected void start(ServerLevel worldIn, PathfinderMob entityIn, long gameTimeIn) {
        if (entityIn instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entityIn;
            if (maid.isInSittingPose()) {
                maid.setInSittingPose(false);
            }
            if (maid.isPassenger()) {
                maid.stopRiding();
            }
        }
        super.start(worldIn, entityIn, gameTimeIn);
    }
}