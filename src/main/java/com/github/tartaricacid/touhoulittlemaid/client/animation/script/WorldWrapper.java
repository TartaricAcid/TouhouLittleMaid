package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IWorldData;
import net.minecraft.world.World;

public class WorldWrapper implements IWorldData {
    private final World world;

    public WorldWrapper(World world) {
        this.world = world;
    }

    @Override
    public long getWorldTime() {
        return world.getDayTime();
    }

    @Override
    public boolean isDay() {
        return world.getDayTime() < 13000;
    }

    @Override
    public boolean isNight() {
        return world.getDayTime() >= 13000;
    }

    @Override
    public boolean isRaining() {
        return world.isRaining();
    }

    @Override
    public boolean isThundering() {
        return world.isThundering();
    }
}
