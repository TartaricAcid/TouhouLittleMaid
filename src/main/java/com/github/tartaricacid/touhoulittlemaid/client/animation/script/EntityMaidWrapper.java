package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityTrolley;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;

public class EntityMaidWrapper {
    private EntityMaid maid;
    public float swingProgress;
    public boolean isRiding;

    public void setData(EntityMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
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
}
