package com.github.tartaricacid.touhoulittlemaid.capability;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MaidNumCapability {
    private int num = 0;
    private boolean dirty;

    public boolean canAdd() {
        return this.num + 1 <= getMaxNum();
    }

    public void add() {
        this.add(1);
    }

    public void add(int num) {
        if (num + this.num <= getMaxNum()) {
            this.num += num;
        } else {
            this.num = getMaxNum();
        }
        markDirty();
    }

    public void min(int num) {
        if (num <= this.num) {
            this.num -= num;
        } else {
            this.num = 0;
        }
        markDirty();
    }

    public void set(int num) {
        this.num = MathHelper.clamp(num, 0, getMaxNum());
        markDirty();
    }

    public int getMaxNum() {
        return MaidConfig.OWNER_MAX_MAID_NUM.get();
    }

    public int get() {
        return this.num;
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public static class Storage implements Capability.IStorage<MaidNumCapability> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<MaidNumCapability> capability, MaidNumCapability instance, Direction side) {
            return IntNBT.valueOf(instance.get());
        }

        @Override
        public void readNBT(Capability<MaidNumCapability> capability, MaidNumCapability instance, Direction side, INBT nbt) {
            instance.set(((IntNBT) nbt).getAsInt());
        }
    }
}
