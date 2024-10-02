package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ride;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;

public class MaidRideBegTask extends Behavior<EntityMaid> {
    private static final int BEG_DISTANCE = 6;

    public MaidRideBegTask() {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return owner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).map(list ->
                        list.find(owner::isOwnedBy)
                                .filter(LivingEntity::isAlive)
                                .filter(e -> e.closerThan(owner, BEG_DISTANCE))
                                .filter(e -> owner.isWithinRestriction(e.blockPosition()))
                                .anyMatch((e) -> holdTemptationItem(owner, e)))
                .orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        return checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        LivingEntity owner = maid.getOwner();
        if (owner instanceof Player) {
            BehaviorUtils.lookAtEntity(maid, owner);
            maid.setBegging(true);
        }
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setBegging(false);
    }

    private boolean holdTemptationItem(EntityMaid owner, LivingEntity e) {
        return owner.getTemptationItem().test(e.getMainHandItem());
    }
}
