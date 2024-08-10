package com.github.tartaricacid.touhoulittlemaid.api.animation;

import net.minecraft.world.level.biome.Biome;

public interface IMaidData extends IEntityData {
    /**
     * Get maid's work task register name
     *
     * @return String
     */
    String getTask();

    /**
     * Whether the maid wears helmet
     *
     * @return boolean
     */
    boolean hasHelmet();

    /**
     * Get maid's helmet register name
     *
     * @return If maid not wearing helmet, return an empty string
     */
    String getHelmet();

    /**
     * Whether the maid wears chest plate
     *
     * @return boolean
     */
    boolean hasChestPlate();

    /**
     * Get maid's chest plate register name
     *
     * @return If maid not wearing chest plate, return an empty string
     */
    String getChestPlate();

    /**
     * Whether the maid wears leggings
     *
     * @return boolean
     */
    boolean hasLeggings();

    /**
     * Get maid's leggings register name
     *
     * @return If maid not wearing leggings, return an empty string
     */
    String getLeggings();

    /**
     * Whether the maid wears boots
     *
     * @return boolean
     */
    boolean hasBoots();

    /**
     * Get maid's boots register name
     *
     * @return If maid not wearing boots, return an empty string
     */
    String getBoots();

    /**
     * Whether the maid main hand hold item
     *
     * @return boolean
     */
    boolean hasItemMainhand();

    /**
     * Get maid main hand item's register name
     *
     * @return If the maid main hand doesn't have any items, return an empty string
     */
    String getItemMainhand();

    /**
     * Whether the maid off hand hold item
     *
     * @return boolean
     */
    boolean hasItemOffhand();

    /**
     * Get maid off hand item's register name
     *
     * @return If the maid off hand doesn't have any items, return an empty string
     */
    String getItemOffhand();

    /**
     * Whether the maid is in beg
     *
     * @return boolean
     */
    boolean isBegging();

    /**
     * Whether the maid is swinging arms
     *
     * @return boolean
     */
    boolean isSwingingArms();

    /**
     * Whether the maid is in riding
     *
     * @return boolean
     */
    boolean isRiding();

    /**
     * Whether the maid is in sitting
     *
     * @return boolean
     */
    boolean isSitting();

    /**
     * Whether the maid wear backpack
     *
     * @return boolean
     */
    boolean hasBackpack();

    /**
     * Get maid's backpack level
     *
     * @return int
     */
    int getBackpackLevel();

    /**
     * Whether the maid is in water
     *
     * @return boolean
     */
    boolean inWater();

    /**
     * Whether the maid is in rain
     *
     * @return boolean
     */
    boolean inRain();

    /**
     * Get maid's biome register name
     *
     * @return String
     */
    Biome getAtBiome();

    /**
     * Whether the maid is swinging left arms
     *
     * @return boolean
     */
    boolean isSwingLeftHand();

    /**
     * Get maid's swinging time
     *
     * @return float
     */
    float getSwingProgress();

    /**
     * Get maid's health
     *
     * @return float
     */
    float getHealth();

    /**
     * Get maid's max health
     *
     * @return float
     */
    float getMaxHealth();

    /**
     * Get the total armor value of the maid
     *
     * @return double
     */
    double getArmorValue();

    /**
     * Whether the maid is on hurt
     *
     * @return boolean
     */
    boolean onHurt();

    /**
     * Whether the maid is sleep
     *
     * @return boolean
     */
    boolean isSleep();

    /**
     * Get the maid's favorability
     *
     * @return int
     */
    int getFavorability();

    /**
     * Whether the maid is on ground
     *
     * @return boolean
     */
    boolean isOnGround();

    /**
     * Whether the maid has sasimono
     *
     * @return boolean
     * @deprecated 1.16 no sasimono
     */
    @Deprecated
    boolean hasSasimono();

    /**
     * Whether the maid hold trolley
     *
     * @return boolean
     * @deprecated 1.16 no trolley
     */
    @Deprecated
    boolean isHoldTrolley();

    /**
     * Whether the maid riding marisa broom
     *
     * @return boolean
     * @deprecated 1.16 no marisa broom
     */
    @Deprecated
    boolean isRidingMarisaBroom();

    /**
     * Whether the maid hold vehicle
     *
     * @return boolean
     * @deprecated 1.16 no vehicle
     */
    @Deprecated
    boolean isHoldVehicle();

    /**
     * Whether the maid hold portable audio and play it
     *
     * @return boolean
     * @deprecated 1.16 no portable audio
     */
    @Deprecated
    boolean isPortableAudioPlay();

    /**
     * When the maid hold vehicle, left hand's rotation
     *
     * @return float[3]{xRot, yRot, zRot}
     * @deprecated 1.16 no vehicle
     */
    @Deprecated
    float[] getLeftHandRotation();

    /**
     * When the maid hold vehicle, right hand's rotation
     *
     * @return float[3]{xRot, yRot, zRot}
     * @deprecated 1.16 no vehicle
     */
    @Deprecated
    float[] getRightHandRotation();

    /**
     * Get maid's biome temperature enum
     *
     * @return warm hot ocean cold
     * @deprecated 1.16 no biome temperature enum
     */
    @Deprecated
    String getAtBiomeTemp();

    /**
     * Whether the maid is in riding the player
     *
     * @return boolean
     */
    @Deprecated
    boolean isRidingPlayer();
}
