package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.RunAwayTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;

public class MaidRunAwayTask<T> extends RunAwayTask<T> {
    public MaidRunAwayTask(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag, Function<T, Vector3d> toPosition) {
        super(walkAwayFromMemory, speedModifier, desiredDistance, flag, toPosition);
    }

    public static MaidRunAwayTask<BlockPos> pos(MemoryModuleType<BlockPos> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, flag, Vector3d::atBottomCenterOf);
    }

    public static MaidRunAwayTask<? extends Entity> entity(MemoryModuleType<? extends Entity> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, flag, Entity::position);
    }

    @Override
    protected void start(ServerWorld worldIn, CreatureEntity entityIn, long gameTimeIn) {
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