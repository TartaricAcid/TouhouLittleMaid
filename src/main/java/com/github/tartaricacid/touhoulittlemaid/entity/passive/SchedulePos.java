package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public final class SchedulePos {
    private BlockPos workPos;
    private BlockPos idlePos;
    private BlockPos sleepPos;
    private ResourceLocation dimension;
    private boolean configured = false;

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

    public void save(CompoundNBT compound) {
        CompoundNBT data = new CompoundNBT();
        data.put("Work", NBTUtil.writeBlockPos(this.workPos));
        data.put("Idle", NBTUtil.writeBlockPos(this.idlePos));
        data.put("Sleep", NBTUtil.writeBlockPos(this.sleepPos));
        data.putString("Dimension", this.dimension.toString());
        data.putBoolean("Configured", this.configured);
        compound.put("MaidSchedulePos", data);
    }

    public void load(CompoundNBT compound, EntityMaid maid) {
        if (compound.contains("MaidSchedulePos", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT data = compound.getCompound("MaidSchedulePos");
            this.workPos = NBTUtil.readBlockPos(data.getCompound("Work"));
            this.idlePos = NBTUtil.readBlockPos(data.getCompound("Idle"));
            this.sleepPos = NBTUtil.readBlockPos(data.getCompound("Sleep"));
            this.dimension = new ResourceLocation(data.getString("Dimension"));
            this.configured = data.getBoolean("Configured");
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
}
