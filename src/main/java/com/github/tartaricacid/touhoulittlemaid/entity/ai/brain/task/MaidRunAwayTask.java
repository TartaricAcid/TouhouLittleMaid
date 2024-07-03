package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class MaidRunAwayTask<T> extends SetWalkTargetAwayFrom<T> {
    public MaidRunAwayTask(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget, Function<T, Vec3> toPosition) {
        super(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, toPosition);
    }

    public static MaidRunAwayTask<BlockPos> pos(MemoryModuleType<BlockPos> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, Vec3::atBottomCenterOf);
    }

    public static MaidRunAwayTask<? extends Entity> entity(MemoryModuleType<? extends Entity> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, Entity::position);
    }

    @Override
    protected void start(ServerLevel worldIn, PathfinderMob entityIn, long gameTimeIn) {
        if (entityIn instanceof EntityMaid maid) {
            if (maid.isMaidInSittingPose()) {
                maid.setInSittingPose(false);
            }
            if (maid.isPassenger()) {
                maid.stopRiding();
            }
        }
        super.start(worldIn, entityIn, gameTimeIn);
    }
}