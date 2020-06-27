package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityTrolley;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;

public class EntityMaidWrapper {
    public float swingProgress;
    public boolean isRiding;
    private EntityMaid maid;
    private WorldWrapper world;

    public void setData(EntityMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
        this.world = new WorldWrapper(maid.world);
    }

    public WorldWrapper getWorld() {
        return world;
    }

    public String getTask() {
        return maid.getTask().getUid().getPath();
    }

    public boolean hasHelmet() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
    }

    public boolean hasChestPlate() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
    }

    public boolean hasLeggings() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
    }

    public boolean hasBoots() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
    }

    public boolean isBegging() {
        return maid.isBegging();
    }

    public boolean isSwingingArms() {
        return maid.isSwingingArms();
    }

    public boolean isRiding() {
        return isRiding;
    }

    public boolean isSitting() {
        return maid.isSitting();
    }

    public boolean isHoldTrolley() {
        return maid.getControllingPassenger() instanceof AbstractEntityTrolley;
    }

    public boolean isRidingMarisaBroom() {
        return maid.getControllingPassenger() instanceof EntityMarisaBroom || maid.isDebugBroomShow;
    }

    public boolean isRidingPlayer() {
        return maid.getRidingEntity() instanceof EntityPlayer;
    }

    public boolean isHoldVehicle() {
        return maid.getControllingPassenger() instanceof EntityMaidVehicle;
    }

    public boolean isSwingLeftHand() {
        return maid.swingingHand == EnumHand.OFF_HAND;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public float[] getLeftHandRotation() {
        if (maid.getControllingPassenger() instanceof EntityMaidVehicle) {
            return ((EntityMaidVehicle) maid.getControllingPassenger()).getMaidHandRotation(EnumHand.OFF_HAND);
        }
        return new float[]{0, 0, 0};
    }

    public float[] getRightHandRotation() {
        if (maid.getControllingPassenger() instanceof EntityMaidVehicle) {
            return ((EntityMaidVehicle) maid.getControllingPassenger()).getMaidHandRotation(EnumHand.MAIN_HAND);
        }
        return new float[]{0, 0, 0};
    }

    public int getDim() {
        return maid.dimension;
    }

    public float getHealth() {
        return maid.getHealth();
    }

    public float getMaxHealth() {
        return maid.getMaxHealth();
    }
}
