package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import net.minecraft.world.World;

public class WorldWrapper {
    private final World world;

    public WorldWrapper(World world) {
        this.world = world;
    }

    public long getWorldTime() {
        return world.getWorldTime();
    }

    public boolean isDay() {
        return world.isDaytime();
    }

    public boolean isNight() {
        return !world.isDaytime();
    }

    public boolean isRaining() {
        return world.isRaining();
    }

    public boolean isThundering() {
        return world.isThundering();
    }
}
