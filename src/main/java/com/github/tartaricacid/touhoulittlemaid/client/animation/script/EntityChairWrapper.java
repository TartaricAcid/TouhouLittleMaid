package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IChairData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.PlayerEntity;

public class EntityChairWrapper implements IChairData {
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

    @Override
    public boolean isRidingPlayer() {
        return chair.getControllingPassenger() instanceof PlayerEntity;
    }

    @Override
    public boolean hasPassenger() {
        return !chair.getPassengers().isEmpty();
    }

    @Override
    public float getPassengerYaw() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).yRot;
        }
        return 0;
    }

    @Override
    public float getYaw() {
        return chair.yRot;
    }

    @Override
    public float getPassengerPitch() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).xRot;
        }
        return 0;
    }

    @Override
    @Deprecated
    public int getDim() {
        return chair.getDim();
    }

    @Override
    public WorldWrapper getWorld() {
        return world;
    }

    @Override
    public long getSeed() {
        return chair.getUUID().getLeastSignificantBits();
    }
}
