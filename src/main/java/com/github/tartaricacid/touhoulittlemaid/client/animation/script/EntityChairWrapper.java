package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.PlayerEntity;

public class EntityChairWrapper {
    private EntityChair chair;
    private WorldWrapper world;

    public void setData(EntityChair chair) {
        this.chair = chair;
        this.world = new WorldWrapper(chair.level);
    }

    public void clearData() {
        this.chair = null;
        this.world = null;
    }

    public boolean isRidingPlayer() {
        return chair.getControllingPassenger() instanceof PlayerEntity;
    }

    public boolean hasPassenger() {
        return !chair.getPassengers().isEmpty();
    }

    public float getPassengerYaw() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).yRot;
        }
        return 0;
    }

    public float getYaw() {
        return chair.yRot;
    }

    public float getPassengerPitch() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).xRot;
        }
        return 0;
    }

    @Deprecated
    public int getDim() {
        return chair.getDim();
    }

    public WorldWrapper getWorld() {
        return world;
    }
}
