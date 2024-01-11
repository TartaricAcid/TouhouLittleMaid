package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MaidReturnHomeTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 200;
    private static final int TOO_CLOSED_RANGE = 3;
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;
    private final float walkSpeed;

    public MaidReturnHomeTask(float walkSpeed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.walkSpeed = walkSpeed;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        return owner.hasRestriction() && farAway(owner) && super.checkExtraStartConditions(worldIn, owner);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        double distanceSqr = maid.getRestrictCenter().distSqr(maid.blockPosition());
        int minTeleportDistance = (int) maid.getRestrictRadius() + 4;
        if (distanceSqr > (minTeleportDistance * minTeleportDistance)) {
            teleportToPos(maid);
        } else {
            BrainUtil.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), walkSpeed, 1);
        }
    }

    private void teleportToPos(EntityMaid maid) {
        BlockPos blockPos = maid.getRestrictCenter();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            int x = this.randomIntInclusive(maid.getRandom(), -3, 3);
            int y = this.randomIntInclusive(maid.getRandom(), -1, 1);
            int z = this.randomIntInclusive(maid.getRandom(), -3, 3);
            if (maybeTeleportTo(maid, blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z)) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(EntityMaid maid, int x, int y, int z) {
        if (!canTeleportTo(maid, new BlockPos(x, y, z))) {
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

    private boolean canTeleportTo(EntityMaid maid, BlockPos pos) {
        PathNodeType pathNodeType = WalkAndSwimNodeProcessor.getBlockPathTypeStatic(maid.level, pos.mutable());
        // Fixme: 水面也可以传送?
        if (pathNodeType == PathNodeType.WALKABLE) {
            BlockPos blockPos = pos.subtract(maid.blockPosition());
            return maid.level.noCollision(maid, maid.getBoundingBox().move(blockPos));
        }
        return false;
    }

    private int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private boolean farAway(EntityMaid maid) {
        return maid.getRestrictCenter().distSqr(maid.blockPosition()) > (TOO_CLOSED_RANGE * TOO_CLOSED_RANGE);
    }
}
