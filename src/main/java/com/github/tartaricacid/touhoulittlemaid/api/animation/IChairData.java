package com.github.tartaricacid.touhoulittlemaid.api.animation;

public interface IChairData extends IEntityData {
    /**
     * Whether the player is riding on the chair
     *
     * @return boolean
     */
    boolean isRidingPlayer();

    /**
     * Whether there is a riding entity on the chair
     *
     * @return boolean
     */
    boolean hasPassenger();

    /**
     * Get passenger's yaw
     *
     * @return float
     */
    float getPassengerYaw();

    /**
     * Get passenger's pitch
     *
     * @return float
     */
    float getPassengerPitch();

    /**
     * Get self's yaw
     *
     * @return float
     */
    float getYaw();
}
