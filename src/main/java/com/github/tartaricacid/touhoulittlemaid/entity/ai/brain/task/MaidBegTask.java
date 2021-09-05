package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class MaidBegTask extends Task<EntityMaid> {
    public MaidBegTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        return owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET)
                .filter(LivingEntity::isAttackable)
                .filter(owner::isOwnedBy)
                .filter((e) -> e.getMainHandItem().getItem() == owner.getTemptationItem())
                .isPresent();
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        return checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        PlayerEntity player = (PlayerEntity) maid.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (!maid.isInSittingPose()) {
            BrainUtil.setWalkAndLookTargetMemories(maid, player, 0.5f, 2);
        }
        maid.setBegging(true);
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid owner, long gameTime) {
        PlayerEntity player = (PlayerEntity) owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (owner.distanceToSqr(player) > 4.0D) {
            if (!owner.isInSittingPose()) {
                BrainUtil.setWalkAndLookTargetMemories(owner, player, 0.5f, 2);
            }
        }
        if (!owner.isBegging()) {
            owner.setBegging(true);
        }
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
        entityIn.setBegging(false);
    }
}
