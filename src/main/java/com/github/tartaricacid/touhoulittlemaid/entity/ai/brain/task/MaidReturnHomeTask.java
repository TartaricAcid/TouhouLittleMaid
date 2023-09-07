package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class MaidReturnHomeTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 100;
    private static final int TOO_CLOSED_RANGE = 3;
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;
    private static final int MIN_TELEPORT_DISTANCE = 12;
    private final float walkSpeed;

    public MaidReturnHomeTask(float walkSpeed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.walkSpeed = walkSpeed;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return owner.hasRestriction() && farAway(owner) && super.checkExtraStartConditions(worldIn, owner);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        double distanceSqr = maid.getRestrictCenter().distSqr(maid.blockPosition());
        if (distanceSqr > (MIN_TELEPORT_DISTANCE * MIN_TELEPORT_DISTANCE)) {
            teleportToPos(maid);
        } else {
            BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), walkSpeed, 1);
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
            maid.moveTo(x + 0.5, y, z + 0.5, maid.getYRot(), maid.getXRot());
            maid.getNavigation().stop();
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.PATH);
            return true;
        }
    }

    private boolean canTeleportTo(EntityMaid maid, BlockPos pos) {
        BlockPathTypes pathNodeType = WalkNodeEvaluator.getBlockPathTypeStatic(maid.level, pos.mutable());
        if (pathNodeType == BlockPathTypes.WALKABLE) {
            BlockPos blockPos = pos.subtract(maid.blockPosition());
            return maid.level.noCollision(maid, maid.getBoundingBox().move(blockPos));
        }
        return false;
    }

    private int randomIntInclusive(RandomSource random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private boolean farAway(EntityMaid maid) {
        return maid.getRestrictCenter().distSqr(maid.blockPosition()) > (TOO_CLOSED_RANGE * TOO_CLOSED_RANGE);
    }
}
