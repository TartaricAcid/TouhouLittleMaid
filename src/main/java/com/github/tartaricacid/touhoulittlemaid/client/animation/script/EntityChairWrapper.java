package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IChairData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.EntityPlayer;

public final class EntityChairWrapper implements IChairData {
    private EntityChair chair;
    private final WorldWrapper world = new WorldWrapper();

    public void setData(EntityChair chair) {
        this.chair = chair;
        this.world.setData(chair.world);
    }

    public void clearData() {
        this.chair = null;
        this.world.clearData();
    }

    @Override
    public boolean isRidingPlayer() {
        return chair.getRidingEntity() instanceof EntityPlayer;
    }

    @Override
    public boolean hasPassenger() {
        return !chair.getPassengers().isEmpty();
    }

    @Override
    public float getPassengerYaw() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).rotationYaw;
        }
        return 0;
    }

    @Override
    public float getYaw() {
        return chair.rotationYaw;
    }

    @Override
    public float getPassengerPitch() {
        if (!chair.getPassengers().isEmpty()) {
            return chair.getPassengers().get(0).rotationPitch;
        }
        return 0;
    }

    @Override
    public int getDim() {
        return chair.dimension;
    }

    @Override
    public long getSeed() {
        return Math.abs(chair.getUniqueID().getLeastSignificantBits());
    }

    @Override
    public WorldWrapper getWorld() {
        return world;
    }
}
