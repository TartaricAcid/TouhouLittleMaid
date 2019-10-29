package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.util.math.MathHelper;

import java.util.concurrent.Callable;

/**
 * @author TartaricAcid
 * @date 2019/8/28 16:40
 **/
public class PowerHandler {
    public static final float MAX_POWER = 5.0f;
    static Factory FACTORY = new Factory();
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

    private static class Factory implements Callable<PowerHandler> {
        @Override
        public PowerHandler call() {
            return new PowerHandler();
        }
    }
}
