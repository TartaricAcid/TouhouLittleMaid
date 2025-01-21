package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.mixin.EntityAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaidFollowOwnerVehicleTask extends Behavior<EntityMaid> {
    private final float speedModifier;
    private final int stopDistance;
    private Entity ownerControlledVehicle;
    private Type type = Type.NONE;

    public MaidFollowOwnerVehicleTask(float speedModifier, int stopDistance) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        // 必须是跟随模式并且自身可以移动
        if (!this.maidStateConditions(maid)) {
            return false;
        }

        // 主人必须在场
        LivingEntity owner = maid.getOwner();
        if (!this.ownerStateConditions(owner)) {
            return false;
        }

        Entity ownerControlledVehicle = owner.getControlledVehicle();
        Entity maidVehicle = maid.getVehicle();
        // 如果主人下船(载具)了，女仆也下船
        // 反之上船了，女仆也跟着上船
        // 当然，这个载具必须还有空位才可以
        if (ownerControlledVehicle == null) {
            if (maid.isPassenger()) {
                this.type = Type.STOP;
                return true;
            } else {
                return false;
            }
        } else if (maidVehicle != null && maidVehicle == ownerControlledVehicle) {
            return false;
        } else if (!((EntityAccessor) ownerControlledVehicle).tlmCanAddPassenger(maid)) {
            return false;
        }

        if (maid.closerThan(ownerControlledVehicle, 5)) {
            this.ownerControlledVehicle = ownerControlledVehicle;
            this.type = Type.RIDE;
            return true;
        } else if (!maid.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            BehaviorUtils.setWalkAndLookTargetMemories(maid, ownerControlledVehicle, speedModifier, stopDistance);
            return false;
        }

        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        switch (this.type) {
            case RIDE -> Optional.of(this.ownerControlledVehicle).ifPresent(maid::startRiding);
            case STOP -> maid.stopRiding();
        }
        maid.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.ownerControlledVehicle = null;
        this.type = Type.NONE;
    }

    private boolean canBrainMoving(EntityMaid maid) {
        return !maid.isMaidInSittingPose() && !maid.isSleeping();
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isHomeModeEnable() && this.canBrainMoving(maid) && maid.isRideable();
    }

    private boolean ownerStateConditions(@Nullable LivingEntity owner) {
        return owner != null && owner.isAlive() && !owner.isSpectator() && !owner.isDeadOrDying();
    }

    enum Type {
        RIDE,
        STOP,
        NONE;
    }
}
