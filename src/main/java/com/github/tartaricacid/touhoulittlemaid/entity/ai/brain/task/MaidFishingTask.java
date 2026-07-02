package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.FishingTypeManager;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.WaterBodyDetector;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.WaterBodyDetector.Result;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.WaterBodyDetector.WaterBody;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class MaidFishingTask extends MaidCheckRateTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaidFishingTask.class);
    private static final int MAX_DELAY_TIME = 100;
    private static final int REVALIDATE_DELAY = 200;
    private static final int RECAST_COOLDOWN_MIN = 30;
    private static final int RECAST_COOLDOWN_MAX = 60;

    @Nullable
    private WaterBody waterBody;
    @Nullable
    private BlockPos shorePos;
    @Nullable
    private BlockPos castTarget;
    private int revalidateTimer;
    private int consecutiveFailures;
    private int fishingDurationTicks;
    private int recastCooldown;
    private int waterStuckTicks;

    private static final int SIT_THRESHOLD_TICKS = 600;

    public MaidFishingTask() {
        super(ImmutableMap.of(), 864000);
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        if (maid.fishing != null) {
            return false;
        }
        if (isOwnerTooFar(maid)) {
            if (maid.getVehicle() instanceof EntitySit) {
                maid.stopRiding();
            }
            return false;
        }
        if (!maid.canBrainMoving() && !(maid.getVehicle() instanceof EntitySit)) {
            return false;
        }

        ItemStack mainHandItem = maid.getMainHandItem();
        IFishingType fishingType = FishingTypeManager.getFishingType(mainHandItem);
        if (!fishingType.isFishingRod(mainHandItem)) {
            return false;
        }

        if (recastCooldown > 0) {
            recastCooldown--;
            return false;
        }

        if (maid.isInWater()) {
            if (++waterStuckTicks > 200) {
                LOGGER.warn("MaidFishingTask: stuck in water for {} ticks, re-searching", waterStuckTicks);
                waterStuckTicks = 0;
                revalidateTimer = 0;
                forgetWaterBody();
            } else if (shorePos != null && maid.isWithinRestriction(shorePos)) {
                LOGGER.info("MaidFishingTask: in water, walking back to shore {}", shorePos);
                BehaviorUtils.setWalkAndLookTargetMemories(maid, shorePos, 0.6f, 0);
                this.setNextCheckTickCount(5);
            }
            return false;
        }
        waterStuckTicks = 0;

        if (waterBody != null && shorePos != null && castTarget != null && isCloseToShore(maid) && maid.isWithinRestriction(shorePos)) {
            LOGGER.info("MaidFishingTask: recast, shore={} target={}", shorePos, castTarget);
            return true;
        }

        if (waterBody != null && shorePos != null) {
            float range = maid.getRestrictRadius();
            double dist = Math.sqrt(maid.blockPosition().distSqr(shorePos));
            if (dist > range) {
                LOGGER.info("MaidFishingTask: shore too far ({} > {}), re-searching", dist, range);
                forgetWaterBody();
                return false;
            }
        }

        if (!super.checkExtraStartConditions(level, maid)) {
            return false;
        }

        if (++revalidateTimer > REVALIDATE_DELAY) {
            revalidateTimer = 0;
            forgetWaterBody();
        }

        if (needReSearch()) {
            LOGGER.info("MaidFishingTask: searching for water body at {}", maid.getBrainSearchPos());
            searchForWaterBody(level, maid);
        }

        if (waterBody == null || shorePos == null || castTarget == null) {
            LOGGER.info("MaidFishingTask: no water body found");
            return false;
        }

        if (!maid.isWithinRestriction(shorePos)) {
            LOGGER.info("MaidFishingTask: shore {} outside restriction", shorePos);
            forgetWaterBody();
            return false;
        }

        if (!isCloseToShore(maid)) {
            double dx = shorePos.getCenter().x - maid.getX();
            double dz = shorePos.getCenter().z - maid.getZ();
            LOGGER.info("MaidFishingTask: walking to shore {} center={}, maid=({},{},{}) dist={}", shorePos, castTarget, maid.getX(), maid.getY(), maid.getZ(), Math.sqrt(dx*dx+dz*dz));
            BehaviorUtils.setWalkAndLookTargetMemories(maid, shorePos, 0.6f, 1);
            this.setNextCheckTickCount(5);
            return false;
        }

        LOGGER.info("MaidFishingTask: ready to cast! shore={} target={}", shorePos, castTarget);
        return true;
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        if (waterBody == null || castTarget == null) {
            LOGGER.warn("MaidFishingTask: start() called but waterBody={} castTarget={}, aborting", waterBody, castTarget);
            return;
        }

        ItemStack mainHandItem = maid.getMainHandItem();
        IFishingType fishingType = FishingTypeManager.getFishingType(mainHandItem);
        if (!fishingType.isFishingRod(mainHandItem) || !fishingType.suitableFishingHook(maid, level, mainHandItem, castTarget)) {
            consecutiveFailures++;
            LOGGER.warn("MaidFishingTask: suitableFishingHook failed, failures={}", consecutiveFailures);
            if (consecutiveFailures > 3) { forgetWaterBody(); consecutiveFailures = 0; }
            return;
        }

        fishingDurationTicks += MAX_DELAY_TIME;
        LOGGER.info("MaidFishingTask: cast #{} (fishingDurationTicks={}/{})", fishingDurationTicks / MAX_DELAY_TIME, fishingDurationTicks, SIT_THRESHOLD_TICKS);
        if (fishingDurationTicks >= SIT_THRESHOLD_TICKS && !(maid.getVehicle() instanceof EntitySit)) {
            LOGGER.info("MaidFishingTask: sitting down! fishingDurationTicks={}", fishingDurationTicks);
            double dx = castTarget.getX() - shorePos.getX();
            double dz = castTarget.getZ() - shorePos.getZ();
            double offsetX = 0;
            double offsetZ = 0;
            if (Math.abs(dx) > Math.abs(dz)) {
                offsetX = Math.signum(dx) * 0.3;
            } else {
                offsetZ = Math.signum(dz) * 0.3;
            }
            BlockState ground = level.getBlockState(shorePos.below());
            double groundTop;
            if (ground.getBlock() instanceof StairBlock) {
                Direction waterDir = Direction.getNearest(dx, 0, dz);
                Direction stairFacing = ground.getValue(StairBlock.FACING);
                if (stairFacing != waterDir) {
                    groundTop = shorePos.below().getY() + 0.5;
                } else {
                    groundTop = shorePos.below().getY() + 1.0;
                }
            } else {
                VoxelShape shape = ground.getCollisionShape(level, shorePos.below());
                groundTop = shorePos.below().getY() + (shape.isEmpty() ? 1.0 : shape.max(Direction.Axis.Y));
            }
            double yOffset = groundTop - shorePos.getY() - 0.40;
            Vec3 seatPos = Vec3.atCenterOf(shorePos).add(offsetX, yOffset, offsetZ);
            EntitySit seat = new EntitySit(level, seatPos, "fishing", shorePos);
            Direction faceToWater = Direction.getNearest(dx, 0, dz);
            seat.setYRot(faceToWater.toYRot());
            level.addFreshEntity(seat);
            maid.startRiding(seat);
        }

        Vec3 targetVec = Vec3.atCenterOf(castTarget);
        MaidFishingHook hook = fishingType.getFishingHook(maid, level, mainHandItem, targetVec);
        level.addFreshEntity(hook);

        level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        maid.swing(InteractionHand.MAIN_HAND);
        maid.getLookControl().setLookAt(targetVec);

        consecutiveFailures = 0;
        revalidateTimer = 0;

        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, EntityMaid maid, long gameTime) {
        return maid.fishing != null && !isOwnerTooFar(maid);
    }

    private boolean isOwnerTooFar(EntityMaid maid) {
        if (maid.hasRestriction()) {
            return false;
        }
        var owner = maid.getOwner();
        return owner != null && maid.distanceToSqr(owner) > 256;
    }

    @Override
    protected void stop(ServerLevel level, EntityMaid maid, long gameTime) {
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        recastCooldown = RECAST_COOLDOWN_MIN + maid.getRandom().nextInt(RECAST_COOLDOWN_MAX - RECAST_COOLDOWN_MIN + 1);
    }

    private boolean needReSearch() {
        return waterBody == null || shorePos == null || castTarget == null;
    }

    private boolean isCloseToShore(EntityMaid maid) {
        if (shorePos == null) {
            return false;
        }
        double dx = shorePos.getCenter().x - maid.getX();
        double dz = shorePos.getCenter().z - maid.getZ();
        return dx * dx + dz * dz < 4.0;
    }

    private void searchForWaterBody(ServerLevel level, EntityMaid maid) {
        Result result = WaterBodyDetector.scan(level, maid);
        if (result == null) {
            LOGGER.info("MaidFishingTask: no water body found");
            rememberWaterBody(null, null, null);
            return;
        }
        if (result.shore.distSqr(maid.blockPosition()) > 9) {
            fishingDurationTicks = 0;
        }
        LOGGER.info("MaidFishingTask: found water body size={} maxDist={}, shore={}, cast={}",
                result.waterBody.size, result.waterBody.maxDist, result.shore, result.castTarget);
        rememberWaterBody(result.waterBody, result.shore, result.castTarget);
        maid.getLookControl().setLookAt(Vec3.atCenterOf(result.castTarget));
    }

    private void rememberWaterBody(@Nullable WaterBody body, @Nullable BlockPos shore, @Nullable BlockPos cast) {
        this.waterBody = body;
        this.shorePos = shore;
        this.castTarget = cast;
        this.revalidateTimer = 0;
        this.consecutiveFailures = 0;
        this.waterStuckTicks = 0;
    }

    private void forgetWaterBody() {
        rememberWaterBody(null, null, null);
    }
}
