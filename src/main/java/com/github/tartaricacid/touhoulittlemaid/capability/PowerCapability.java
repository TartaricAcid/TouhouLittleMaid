package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.FloatTag;
import net.minecraft.util.Mth;

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
        this.power = Mth.clamp(points, 0, MAX_POWER);
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

    public FloatTag serializeNBT() {
        return FloatTag.valueOf(this.power);
    }

    public void deserializeNBT(FloatTag nbt) {
        this.power = nbt.getAsFloat();
    }
}
