package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.EntityPlayer;

public class EntityChairWrapper {
    private EntityChair chair;
    private WorldWrapper world;

    public void setChair(EntityChair chair) {
        this.chair = chair;
        this.world = new WorldWrapper(chair.world);
    }

    public boolean isRidingPlayer() {
        return chair.getRidingEntity() instanceof EntityPlayer;
    }

    public boolean hasPassenger() {
        return !chair.getPassengers().isEmpty();
    }

    public float getPassengerYaw() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).rotationYaw;
        }
        return 0;
    }

    public float getYaw() {
        return chair.rotationYaw;
    }

    public float getPassengerPitch() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).rotationPitch;
        }
        return 0;
    }

    public int getDim() {
        return chair.dimension;
    }

    public WorldWrapper getWorld() {
        return world;
    }
}
