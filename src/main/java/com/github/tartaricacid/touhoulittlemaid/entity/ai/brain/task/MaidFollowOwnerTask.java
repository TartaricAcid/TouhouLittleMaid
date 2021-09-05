package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MaidFollowOwnerTask extends Task<EntityMaid> {
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;
    private static final int MIN_TELEPORT_DISTANCE = 12;
    private final float speedModifier;
    private final int stopDistance;
    private final int startDistance;
    private LivingEntity owner;
    private float oldWaterCost;

    public MaidFollowOwnerTask(float speedModifier, int startDistance, int stopDistance) {
        super(ImmutableMap.of());
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid maid) {
        LivingEntity livingentity = maid.getOwner();
        if (maid.isHomeModeEnable() || livingentity == null || livingentity.isSpectator() || maid.isOrderedToSit()
                || maid.distanceToSqr(livingentity) < this.startDistance * this.startDistance) {
            return false;
        }
        this.owner = livingentity;
        return true;
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        if (maid.getBrain().getMemory(MemoryModuleType.PATH).filter(Path::isDone).isPresent()
                || maid.isInSittingPose() || this.owner.isDeadOrDying()) {
            return false;
        }
        return maid.distanceToSqr(this.owner) > this.stopDistance * this.stopDistance;
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        this.oldWaterCost = maid.getPathfindingMalus(PathNodeType.WATER);
        maid.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid maid, long gameTime) {
        if (!maid.isLeashed() && !maid.isPassenger()) {
            if (maid.distanceTo(this.owner) >= MIN_TELEPORT_DISTANCE) {
                this.teleportToOwner(maid);
            } else {
                BrainUtil.setWalkAndLookTargetMemories(maid, owner, speedModifier, stopDistance);
            }
        }
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        this.owner = null;
        maid.getNavigation().stop();
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.PATH);
        maid.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    private void teleportToOwner(EntityMaid maid) {
        BlockPos blockpos = this.owner.blockPosition();

        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            int j = this.randomIntInclusive(maid.getRandom(), -3, 3);
            int k = this.randomIntInclusive(maid.getRandom(), -1, 1);
            int l = this.randomIntInclusive(maid.getRandom(), -3, 3);
            if (maybeTeleportTo(maid, blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l)) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(EntityMaid maid, int x, int y, int z) {
        if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(maid, new BlockPos(x, y, z))) {
            return false;
        } else {
            maid.moveTo(x + 0.5D, y, z + 0.5D, maid.yRot, maid.xRot);
            maid.getNavigation().stop();
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.PATH);
            return true;
        }
    }

    private boolean canTeleportTo(EntityMaid maid, BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(maid.level, pos.mutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = maid.level.getBlockState(pos.below());
            if (blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(maid.blockPosition());
                return maid.level.noCollision(maid, maid.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
