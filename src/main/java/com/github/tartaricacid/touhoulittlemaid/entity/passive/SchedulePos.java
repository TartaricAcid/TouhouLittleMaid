package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.schedule.Activity;

public final class SchedulePos {
    private BlockPos workPos;
    private BlockPos idlePos;
    private BlockPos sleepPos;
    private boolean configured = false;

    public SchedulePos(BlockPos workPos, BlockPos idlePos, BlockPos sleepPos) {
        this.workPos = workPos;
        this.idlePos = idlePos;
        this.sleepPos = sleepPos;
    }

    public SchedulePos(BlockPos workPos, BlockPos idlePos) {
        this(workPos, idlePos, idlePos);
    }

    public SchedulePos(BlockPos workPos) {
        this(workPos, workPos);
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

    public void save(CompoundTag compound) {
        CompoundTag data = new CompoundTag();
        data.put("Work", NbtUtils.writeBlockPos(this.workPos));
        data.put("Idle", NbtUtils.writeBlockPos(this.idlePos));
        data.put("Sleep", NbtUtils.writeBlockPos(this.sleepPos));
        compound.put("MaidSchedulePos", data);
    }

    public void load(CompoundTag compound, EntityMaid maid) {
        if (compound.contains("MaidSchedulePos", Tag.TAG_COMPOUND)) {
            CompoundTag data = compound.getCompound("MaidSchedulePos");
            this.workPos = NbtUtils.readBlockPos(data.getCompound("Work"));
            this.idlePos = NbtUtils.readBlockPos(data.getCompound("Idle"));
            this.sleepPos = NbtUtils.readBlockPos(data.getCompound("Sleep"));
            this.restrictTo(maid);
        }
    }

    public void restrictTo(EntityMaid maid) {
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

    public void clear(EntityMaid maid) {
        this.idlePos = this.workPos;
        this.sleepPos = this.workPos;
        this.configured = false;
        this.restrictTo(maid);
    }

    public void setHomeModeEnable(EntityMaid maid, BlockPos pos) {
        if (!this.configured) {
            this.workPos = pos;
            this.idlePos = pos;
            this.sleepPos = pos;
        }
        this.restrictTo(maid);
    }
}
