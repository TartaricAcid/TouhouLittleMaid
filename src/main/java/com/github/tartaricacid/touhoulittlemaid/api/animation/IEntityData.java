package com.github.tartaricacid.touhoulittlemaid.api.animation;

public interface IEntityData {
    /**
     * Get entity's world data
     *
     * @return IWorldData
     */
    IWorldData getWorld();

    /**
     * Get entity's dimension id
     *
     * @return int
     * @deprecated In 1.16, dimension no longer uses numbers as ids
     */
    @Deprecated
    int getDim();

    /**
     * Get a fixed value, each entity is different, similar to the entity's UUID
     *
     * @return Entity's uuid least significant bits
     */
    long getSeed();
}
