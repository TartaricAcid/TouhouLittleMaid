package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.IPlayerMixin;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.EntityAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaidFollowOwnerVehicleTask extends Behavior<EntityMaid> {
    private static final int RANGE = 3;
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
        if (!this.ownerStateConditions(owner) || !(owner instanceof Player player)) {
            return false;
        }

        Entity ownerControlledVehicle = owner.getControlledVehicle();
        Entity maidVehicle = maid.getVehicle();

        // 如果主人下船（载具）了，女仆也下船。反之上船了，女仆也跟着上船
        // 当然，这个载具必须还有空位才可以
        if (ownerControlledVehicle == null) {
            // 玩家下船有大约 60 tick 冷却时间，在此时间内，一定范围内的女仆才能主动下船，避免误伤
            boolean isCooldown = ((IPlayerMixin) player).tlmInRemoveVehicleCooldown();
            boolean maidInDismountRange = maid.distanceTo(owner) < RANGE;
            if (maid.isPassenger() && isCooldown && maidInDismountRange) {
                this.type = Type.STOP;
                return true;
            }
            return false;
        }

        // 玩家和女仆同坐一艘船，不需要判断
        if (maidVehicle != null && maidVehicle == ownerControlledVehicle) {
            return false;
        }

        // 乘坐的载具不能添加新的乘客了，不执行
        if (!((EntityAccessor) ownerControlledVehicle).tlmCanAddPassenger(maid)) {
            return false;
        }

        // 女仆开始尝试骑乘载具
        if (maid.closerThan(ownerControlledVehicle, RANGE)) {
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
            case RIDE -> Optional.ofNullable(this.ownerControlledVehicle).ifPresent(maid::startRiding);
            case STOP -> maid.stopRiding();
        }
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.ownerControlledVehicle = null;
        this.type = Type.NONE;
    }

    private boolean canBrainMoving(EntityMaid maid) {
        // 不需要判断是否女仆正在骑乘
        return !maid.isMaidInSittingPose() && !maid.isSleeping() && !maid.isLeashed();
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
