package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

import javax.annotation.Nullable;

public final class SchedulePos {
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;

    private BlockPos workPos;
    private BlockPos idlePos;
    private BlockPos sleepPos;
    private ResourceLocation dimension;
    private boolean configured = false;

    public static final StreamCodec<RegistryFriendlyByteBuf, SchedulePos> SCHEDULE_POS_STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,SchedulePos::getWorkPos,
            BlockPos.STREAM_CODEC,SchedulePos::getIdlePos,
            BlockPos.STREAM_CODEC,SchedulePos::getSleepPos,
            ResourceLocation.STREAM_CODEC,SchedulePos::getDimension,
            SchedulePos::new
    );

    public SchedulePos(BlockPos workPos, BlockPos idlePos, BlockPos sleepPos, ResourceLocation dimension) {
        this.workPos = workPos;
        this.idlePos = idlePos;
        this.sleepPos = sleepPos;
        this.dimension = dimension;
    }

    public SchedulePos(BlockPos workPos, BlockPos idlePos, ResourceLocation dimension) {
        this(workPos, idlePos, idlePos, dimension);
    }

    public SchedulePos(BlockPos workPos, ResourceLocation dimension) {
        this(workPos, workPos, dimension);
    }

    public void setWorkPos(BlockPos workPos) {
        this.workPos = workPos;
    }

    public void setIdlePos(BlockPos idlePos) {
        this.idlePos = idlePos;
    }

    public void setSleepPos(BlockPos sleepPos) {
        this.sleepPos = sleepPos;
    }

    public void setDimension(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public void tick(EntityMaid maid) {
        if (maid.tickCount % 40 == 0) {
            this.restrictTo(maid);
            if (maid.isWithinRestriction()) {
                return;
            }
            if (!maid.canBrainMoving()) {
                return;
            }
            double distanceSqr = maid.getRestrictCenter().distSqr(maid.blockPosition());
            int minTeleportDistance = (int) maid.getRestrictRadius() + 4;
            if (distanceSqr > (minTeleportDistance * minTeleportDistance) && !this.sameWithRestrictCenter(maid)) {
                teleport(maid);
            } else {
                BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
            }
        }
    }

    public void save(CompoundTag compound) {
        CompoundTag data = new CompoundTag();
        data.put("Work", NbtUtils.writeBlockPos(this.workPos));
        data.put("Idle", NbtUtils.writeBlockPos(this.idlePos));
        data.put("Sleep", NbtUtils.writeBlockPos(this.sleepPos));
        data.putString("Dimension", this.dimension.toString());
        data.putBoolean("Configured", this.configured);
        compound.put("MaidSchedulePos", data);
    }

    public void load(CompoundTag compound, EntityMaid maid) {
        if (compound.contains("MaidSchedulePos", Tag.TAG_COMPOUND)) {
            CompoundTag data = compound.getCompound("MaidSchedulePos");
            var workPosOptional = NbtUtils.readBlockPos(data, "Work");
            workPosOptional.ifPresent(p -> this.workPos = p);
            var idlePosOptional = NbtUtils.readBlockPos(data, "Idle");
            idlePosOptional.ifPresent(p -> this.idlePos = p);
            var sleepPosOptional = NbtUtils.readBlockPos(data, "Sleep");
            sleepPosOptional.ifPresent(p -> this.sleepPos = p);
            this.dimension = ResourceLocation.parse(data.getString("Dimension"));
            this.configured = data.getBoolean("Configured");
            this.restrictTo(maid);
        }
    }

    public void restrictTo(EntityMaid maid) {
        if (!maid.isHomeModeEnable()) {
            return;
        }
        Activity activity = maid.getScheduleDetail();
        if (activity == Activity.WORK) {
            maid.restrictTo(this.workPos, MaidConfig.MAID_WORK_RANGE.get());
            return;
        }
        if (activity == Activity.IDLE) {
            maid.restrictTo(this.idlePos, MaidConfig.MAID_IDLE_RANGE.get());
            return;
        }
        if (activity == Activity.REST) {
            maid.restrictTo(this.sleepPos, MaidConfig.MAID_SLEEP_RANGE.get());
        }
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public BlockPos getWorkPos() {
        return workPos;
    }

    public BlockPos getIdlePos() {
        return idlePos;
    }

    public BlockPos getSleepPos() {
        return sleepPos;
    }

    public boolean isConfigured() {
        return configured;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public void clear(EntityMaid maid) {
        this.idlePos = this.workPos;
        this.sleepPos = this.workPos;
        this.configured = false;
        this.dimension = maid.level.dimension().location();
        this.restrictTo(maid);
    }

    public void setHomeModeEnable(EntityMaid maid, BlockPos pos) {
        if (!this.configured) {
            this.workPos = pos;
            this.idlePos = pos;
            this.sleepPos = pos;
            this.dimension = maid.level.dimension().location();
        }
        this.restrictTo(maid);
    }

    @Nullable
    public BlockPos getNearestPos(EntityMaid maid) {
        if (this.configured) {
            BlockPos pos = this.workPos;
            double workDistance = maid.blockPosition().distSqr(this.workPos);
            double idleDistance = maid.blockPosition().distSqr(this.idlePos);
            double sleepDistance = maid.blockPosition().distSqr(this.sleepPos);
            if (workDistance > idleDistance) {
                pos = this.idlePos;
                workDistance = idleDistance;
            }
            if (workDistance > sleepDistance) {
                pos = this.sleepPos;
            }
            return pos;
        }
        return null;
    }

    private boolean sameWithRestrictCenter(EntityMaid maid) {
        BlockPos restrictCenter = maid.getRestrictCenter();
        return maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET)
                .filter(walkTarget -> walkTarget.getTarget().currentBlockPosition().equals(restrictCenter))
                .isPresent();
    }

    private void teleport(EntityMaid maid) {
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            if (TeleportHelper.teleportToRestrictCenter(maid)) {
                return;
            }
        }
    }
}
