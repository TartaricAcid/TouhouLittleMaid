package com.github.tartaricacid.touhoulittlemaid.api.animation;

public interface IWorldData {
    /**
     * Get the Minecraft's world time, 0-24000
     *
     * @return world time
     */
    long getWorldTime();

    /**
     * Minecraft's world is day?
     *
     * @return boolean
     */
    boolean isDay();

    /**
     * Minecraft's world is night?
     *
     * @return boolean
     */
    boolean isNight();

    /**
     * Minecraft's world is raining?
     *
     * @return boolean
     */
    boolean isRaining();

    /**
     * Minecraft's world is thundering?
     *
     * @return boolean
     */
    boolean isThundering();
}
