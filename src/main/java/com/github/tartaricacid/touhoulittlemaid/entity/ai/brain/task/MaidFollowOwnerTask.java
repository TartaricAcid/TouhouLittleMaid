package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import javax.annotation.Nullable;

public class MaidFollowOwnerTask extends Behavior<EntityMaid> {
    private final float speedModifier;
    private final int stopDistance;

    public MaidFollowOwnerTask(float speedModifier, int stopDistance) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        LivingEntity owner = maid.getOwner();
        if (ownerStateConditions(owner, maid)) {
            // 如果女仆在前往呼吸点，玩家不在水中
            if (maid.getSwimManager().isGoingToBreath()) {
                return !owner.isUnderWater();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        LivingEntity owner = maid.getOwner();

        // 如果女仆在前往呼吸点快要淹死了，那必须就近传送
        // 这个传送会鬼畜，但是没办法，为了救女仆只能这样了
        if (maid.getSwimManager().isGoingToBreath() && ownerStateConditions(owner, maid)
            && maidStateConditions(maid) && maid.teleportToOwner(owner)) {
            maid.getNavigationManager().resetNavigation();
            maid.getSwimManager().setGoingToBreath(false);
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            this.doStop(worldIn, maid, gameTimeIn);
            return;
        }

        // 否则正常传送
        int startDistance = (int) maid.getRestrictRadius() - 2;
        int minTeleportDistance = startDistance + 4;
        if (ownerStateConditions(owner, maid) && maidStateConditions(maid) && !maid.closerThan(owner, startDistance)) {
            if (!maid.closerThan(owner, minTeleportDistance)) {
                maid.teleportToOwner(owner);
                maid.getNavigationManager().resetNavigation();
            } else if (!ownerIsWalkTarget(maid, owner)) {
                BehaviorUtils.setWalkAndLookTargetMemories(maid, owner, speedModifier, stopDistance);
            }
        }
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isHomeModeEnable() && maid.canBrainMoving();
    }

    private boolean ownerStateConditions(@Nullable LivingEntity owner, EntityMaid maid) {
        return owner != null && !owner.isSpectator() && !owner.isDeadOrDying() &&
               // 修复一个过去很多年没解决的 bug —— 女仆神秘传送问题
               // 这个 bug 的原因是，传送时没有检查女仆和主人是否在同一个维度
               maid.level == owner.level;
    }

    private boolean ownerIsWalkTarget(EntityMaid maid, LivingEntity owner) {
        return maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(target -> {
            if (target.getTarget() instanceof EntityTracker tracker) {
                return tracker.getEntity().equals(owner);
            }
            return false;
        }).orElse(false);
    }
}
