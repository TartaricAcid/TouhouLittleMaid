package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class MaidUseShieldTask extends Behavior<EntityMaid> {
    private static final int CHECK_RANGE = 8;

    public MaidUseShieldTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        // 盾牌优先判断，节省性能
        if (!maid.canUseShield()) {
            return false;
        }
        LivingEntity target = maid.getTarget();
        // 如果敌对生物靠太近，盾牌防御
        return target != null && target.isAlive() && target.distanceTo(maid) < CHECK_RANGE;
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        return this.checkExtraStartConditions(serverLevel, maid);
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        maid.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    protected void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        maid.stopUsingItem();
    }
}
