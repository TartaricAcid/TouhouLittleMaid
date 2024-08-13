package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util;

public class RateLimiter {
    private final float interval;
    private float aggregate;
    private float lastRequestTime;

    public RateLimiter(int limitPerSec) {
        this.interval = 1f / limitPerSec;
        this.aggregate = this.interval;
        this.lastRequestTime = 0;
    }

    public boolean request(float time) {
        this.aggregate += time - this.lastRequestTime;
        this.lastRequestTime = time;

        if (this.aggregate < this.interval) {
            return false;
        }

        do {
            this.aggregate -= this.interval;
        } while (this.aggregate >= this.interval);
        return true;
    }
}
