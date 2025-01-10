package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class MaidUseShieldTask extends Behavior<EntityMaid> {
    public MaidUseShieldTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        return this.shouldUseShield(maid);
    }


    private boolean shouldUseShield(EntityMaid maid) {
        LivingEntity target = maid.getTarget();
        // 远程攻击的话就不需要盾牌防护了，毕竟远程攻击需要一直持续使用远程武器而且还在一直走位，就无法腾出另外一只手来使用其他道具了
        return target != null && maid.canUseShield() && !(maid.getTask() instanceof IRangedAttackTask);
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
