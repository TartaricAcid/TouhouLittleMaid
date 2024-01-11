package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class MaidFollowOwnerTask extends Task<EntityMaid> {
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;
    private final float speedModifier;
    private final int stopDistance;

    public MaidFollowOwnerTask(float speedModifier, int stopDistance) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED));
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        LivingEntity owner = maid.getOwner();
        int startDistance = (int) maid.getRestrictRadius() - 2;
        int minTeleportDistance = startDistance + 4;
        if (ownerStateConditions(owner) && maidStateConditions(maid) && !maid.closerThan(owner, startDistance)) {
            if (!maid.closerThan(owner, minTeleportDistance)) {
                teleportToOwner(maid, owner);
            } else if (!ownerIsWalkTarget(maid, owner)) {
                BrainUtil.setWalkAndLookTargetMemories(maid, owner, speedModifier, stopDistance);
            }
        }
    }

    private void teleportToOwner(EntityMaid maid, LivingEntity owner) {
        BlockPos blockPos = owner.blockPosition();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            int x = this.randomIntInclusive(maid.getRandom(), -3, 3);
            int y = this.randomIntInclusive(maid.getRandom(), -1, 1);
            int z = this.randomIntInclusive(maid.getRandom(), -3, 3);
            if (maybeTeleportTo(maid, owner, blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z)) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(EntityMaid maid, LivingEntity owner, int x, int y, int z) {
        if (teleportTooClosed(owner, x, z)) {
            return false;
        } else if (!canTeleportTo(maid, new BlockPos(x, y, z))) {
            return false;
        } else {
            maid.moveTo(x + 0.5, y, z + 0.5, maid.yRot, maid.xRot);
            maid.getNavigation().stop();
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.PATH);
            return true;
        }
    }

    private boolean teleportTooClosed(LivingEntity owner, int x, int z) {
        return Math.abs(x - owner.getX()) < 2 && Math.abs(z - owner.getZ()) < 2;
    }

    private boolean canTeleportTo(EntityMaid maid, BlockPos pos) {
        PathNodeType pathNodeType = WalkAndSwimNodeProcessor.getBlockPathTypeStatic(maid.level, pos.mutable());
        // Fixme: 水面也可以传送
        if (pathNodeType == PathNodeType.WALKABLE) {
            BlockPos blockPos = pos.subtract(maid.blockPosition());
            return maid.level.noCollision(maid, maid.getBoundingBox().move(blockPos));
        }
        return false;
    }

    private int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isHomeModeEnable() && !maid.isInSittingPose() && !maid.isSleeping() && !maid.isLeashed() && !maid.isPassenger();
    }

    private boolean ownerStateConditions(@Nullable LivingEntity owner) {
        return owner != null && !owner.isSpectator() && !owner.isDeadOrDying();
    }

    private boolean ownerIsWalkTarget(EntityMaid maid, LivingEntity owner) {
        return maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(target -> {
            if (target.getTarget() instanceof EntityPosWrapper) {
                return ((EntityPosWrapper) target.getTarget()).entity.equals(owner);
            }
            return false;
        }).orElse(false);
    }
}
