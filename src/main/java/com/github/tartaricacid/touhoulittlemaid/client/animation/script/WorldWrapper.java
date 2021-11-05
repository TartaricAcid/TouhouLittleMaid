package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IWorldData;
import net.minecraft.world.World;

public final class WorldWrapper implements IWorldData {
    private World world;

    public void setData(World world) {
        this.world = world;
    }

    public void clearData() {
        this.world = null;
    }

    @Override
    public long getWorldTime() {
        return world.getWorldTime();
    }

    @Override
    public boolean isDay() {
        return world.getWorldTime() < 13000;
    }

    @Override
    public boolean isNight() {
        return world.getWorldTime() >= 13000;
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
