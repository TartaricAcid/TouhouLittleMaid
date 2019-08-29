package com.github.tartaricacid.touhoulittlemaid.capability;

import java.util.concurrent.Callable;

/**
 * @author TartaricAcid
 * @date 2019/8/28 16:40
 **/
public class PowerHandler {
    private static final float MAX_POWER = 5.0f;
    static Factory FACTORY = new Factory();
    private float power = 0.0f;

    public void add(float points) {
        if (points + this.power <= MAX_POWER) {
            this.power += points;
        } else {
            this.power = 5.0f;
        }
    }

    public void min(float points) {
        if (points <= this.power) {
            this.power -= points;
        } else {
            this.power = 0.0f;
        }
    }

    public void set(float points) {
        this.power = (points > MAX_POWER) ? MAX_POWER : points;
    }

    public float get() {
        return this.power;
    }

    private static class Factory implements Callable<PowerHandler> {
        @Override
        public PowerHandler call() {
            return new PowerHandler();
        }
    }
}
