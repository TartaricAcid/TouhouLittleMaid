package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PowerCapability {
    public static final float MAX_POWER = 5.0f;
    private float power = 0.0f;
    private boolean dirty;

    public void add(float points) {
        if (points + this.power <= MAX_POWER) {
            this.power += points;
        } else {
            this.power = MAX_POWER;
        }
        markDirty();
    }

    public void min(float points) {
        if (points <= this.power) {
            this.power -= points;
        } else {
            this.power = 0.0f;
        }
        markDirty();
    }

    public void set(float points) {
        this.power = MathHelper.clamp(points, 0, MAX_POWER);
        markDirty();
    }

    public float get() {
        return this.power;
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

    public static class Storage implements Capability.IStorage<PowerCapability> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<PowerCapability> capability, PowerCapability instance, Direction side) {
            return FloatNBT.valueOf(instance.get());
        }

        @Override
        public void readNBT(Capability<PowerCapability> capability, PowerCapability instance, Direction side, INBT nbt) {
            instance.set(((FloatNBT) nbt).getAsFloat());
        }
    }
}
