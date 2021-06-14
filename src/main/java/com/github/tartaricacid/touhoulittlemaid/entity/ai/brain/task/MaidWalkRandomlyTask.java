package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.WalkRandomlyTask;
import net.minecraft.world.server.ServerWorld;

public class MaidWalkRandomlyTask extends WalkRandomlyTask {
    public MaidWalkRandomlyTask(float speedModifier) {
        super(speedModifier);
    }

    public MaidWalkRandomlyTask(float speedModifier, int maxHorizontalDistance, int maxVerticalDistance) {
        super(speedModifier, maxHorizontalDistance, maxVerticalDistance);
    }

    @Override
    protected void start(ServerWorld worldIn, CreatureEntity entityIn, long gameTimeIn) {
        if (entityIn instanceof EntityMaid && ((EntityMaid) entityIn).isInSittingPose()) {
            entityIn.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            return;
        }
        super.start(worldIn, entityIn, gameTimeIn);
    }
}
