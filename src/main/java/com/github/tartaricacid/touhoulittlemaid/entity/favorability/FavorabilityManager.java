package com.github.tartaricacid.touhoulittlemaid.entity.favorability;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Map;

public class FavorabilityManager {
    private static final String TAG_NAME = "FavorabilityManagerCounter";
    private final Map<String, Time> counter;

    public FavorabilityManager() {
        this.counter = Maps.newHashMap();
    }

    public void tick() {
        counter.values().forEach(Time::tick);
    }

    public void addCooldown(String type, int time) {
        this.counter.put(type, new Time(time));
    }

    public boolean canAdd(String type) {
        if (this.counter.containsKey(type)) {
            return this.counter.get(type).isZero();
        }
        return true;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag data = new CompoundTag();
        this.counter.forEach((name, time) -> data.putInt(name, time.getTime()));
        compound.put(TAG_NAME, data);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(TAG_NAME, Tag.TAG_COMPOUND)) {
            CompoundTag data = compound.getCompound(TAG_NAME);
            for (String name : data.getAllKeys()) {
                this.counter.put(name, new Time(data.getInt(name)));
            }
        }
    }

    public static class Time {
        private int time;

        public Time(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void tick() {
            if (time > 0) {
                time--;
            }
        }

        public boolean isZero() {
            return this.time <= 0;
        }
    }
}
