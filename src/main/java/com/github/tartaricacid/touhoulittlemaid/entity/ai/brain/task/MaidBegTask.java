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
    private static final int BEG_DISTANCE = 6;
    private static final int CLOSED_DISTANCE = 2;

    public MaidBegTask() {
        super(ImmutableMap.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        return owner.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).map(list ->
                        list.stream().filter(owner::isOwnedBy)
                                .filter(LivingEntity::isAlive)
                                .filter(e -> e.closerThan(owner, BEG_DISTANCE))
                                .filter(e -> owner.isWithinRestriction(e.blockPosition()))
                                .anyMatch((e) -> holdTemptationItem(owner, e)))
                .orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        return checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid maid, long gameTime) {
        LivingEntity owner = maid.getOwner();
        if (owner instanceof PlayerEntity) {
            if (!maid.closerThan(owner, CLOSED_DISTANCE)) {
                BrainUtil.setWalkAndLookTargetMemories(maid, owner, 0.6f, CLOSED_DISTANCE);
            } else {
                BrainUtil.lookAtEntity(maid, owner);
            }
            maid.setBegging(true);
        }
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setBegging(false);
    }

    private boolean holdTemptationItem(EntityMaid owner, LivingEntity e) {
        return owner.getTemptationItem().test(e.getMainHandItem());
    }
}
