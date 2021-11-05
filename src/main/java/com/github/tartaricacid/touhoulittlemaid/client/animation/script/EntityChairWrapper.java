package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IChairData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.PlayerEntity;

public final class EntityChairWrapper implements IChairData {
    private final WorldWrapper world = new WorldWrapper();
    private EntityChair chair;

    public void setData(EntityChair chair) {
        this.chair = chair;
        this.world.setData(chair.level);
    }

    public void clearData() {
        this.chair = null;
        this.world.clearData();
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
    public WorldWrapper getWorld() {
        return world;
    }

    @Override
    public long getSeed() {
        return Math.abs(chair.getUUID().getLeastSignificantBits());
    }

    @Override
    @Deprecated
    public int getDim() {
        return chair.getDim();
    }
}
