package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityTrolley;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public final class EntityMaidWrapper implements IMaidData {
    private final WorldWrapper world = new WorldWrapper();
    private float swingProgress;
    private boolean isRiding;
    private EntityMaid maid;

    public void setData(EntityMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
        this.world.setData(maid.world);
    }

    public void clearData() {
        this.maid = null;
        this.world.clearData();
    }

    @Override
    public WorldWrapper getWorld() {
        return world;
    }

    @Override
    public String getTask() {
        return maid.getTask().getUid().getPath();
    }

    @Override
    public boolean hasHelmet() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
    }

    @Override
    public String getHelmet() {
        ResourceLocation res = maid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasChestPlate() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
    }

    @Override
    public String getChestPlate() {
        ResourceLocation res = maid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasLeggings() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
    }

    @Override
    public String getLeggings() {
        ResourceLocation res = maid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasBoots() {
        return !maid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
    }

    @Override
    public String getBoots() {
        ResourceLocation res = maid.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemMainhand() {
        return maid.getHeldItemMainhand().isEmpty();
    }

    @Override
    public String getItemMainhand() {
        ResourceLocation res = maid.getHeldItemMainhand().getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemOffhand() {
        return maid.getHeldItemOffhand().isEmpty();
    }

    @Override
    public String getItemOffhand() {
        ResourceLocation res = maid.getHeldItemOffhand().getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean isBegging() {
        return maid.isBegging();
    }

    @Override
    public boolean isSwingingArms() {
        return maid.isSwingingArms();
    }

    @Override
    public boolean isRiding() {
        return isRiding;
    }

    @Override
    public boolean isSitting() {
        return maid.isSitting();
    }

    @Override
    public boolean hasBackpack() {
        return maid.getBackLevel() != EntityMaid.EnumBackPackLevel.EMPTY;
    }

    @Override
    public int getBackpackLevel() {
        return maid.getBackLevel().getLevel();
    }

    @Override
    public boolean hasSasimono() {
        return maid.hasSasimono();
    }

    @Override
    public boolean inWater() {
        return maid.isInWater();
    }

    @Override
    public boolean inRain() {
        return maid.world.isRainingAt(maid.getPosition());
    }

    @Override
    public String getAtBiome() {
        ResourceLocation res = BiomeCacheUtil.getCacheBiome(maid).getRegistryName();
        if (res != null) {
            return res.getPath();
        }
        return "";
    }

    @Override
    public String getAtBiomeTemp() {
        return BiomeCacheUtil.getCacheBiome(maid).getTempCategory().name();
    }

    @Override
    public boolean isHoldTrolley() {
        return maid.getControllingPassenger() instanceof AbstractEntityTrolley;
    }

    @Override
    public boolean isRidingMarisaBroom() {
        return maid.getControllingPassenger() instanceof EntityMarisaBroom || maid.isDebugBroomShow;
    }

    @Override
    public boolean isRidingPlayer() {
        return maid.getRidingEntity() instanceof EntityPlayer;
    }

    @Override
    public boolean isHoldVehicle() {
        return maid.getControllingPassenger() instanceof EntityMaidVehicle;
    }

    @Override
    public boolean isPortableAudioPlay() {
        if (maid.getControllingPassenger() instanceof EntityPortableAudio) {
            EntityPortableAudio audio = (EntityPortableAudio) maid.getControllingPassenger();
            return audio.isPlaying();
        }
        return false;
    }

    @Override
    public boolean isSwingLeftHand() {
        return maid.swingingHand == EnumHand.OFF_HAND;
    }

    @Override
    public float getSwingProgress() {
        return swingProgress;
    }

    @Override
    public float[] getLeftHandRotation() {
        if (maid.getControllingPassenger() instanceof EntityMaidVehicle) {
            return ((EntityMaidVehicle) maid.getControllingPassenger()).getMaidHandRotation(EnumHand.OFF_HAND);
        }
        return new float[]{0, 0, 0};
    }

    @Override
    public float[] getRightHandRotation() {
        if (maid.getControllingPassenger() instanceof EntityMaidVehicle) {
            return ((EntityMaidVehicle) maid.getControllingPassenger()).getMaidHandRotation(EnumHand.MAIN_HAND);
        }
        return new float[]{0, 0, 0};
    }

    @Override
    public int getDim() {
        return maid.dimension;
    }

    @Override
    public long getSeed() {
        return Math.abs(maid.getUniqueID().getLeastSignificantBits());
    }

    @Override
    public float getHealth() {
        return maid.getHealth();
    }

    @Override
    public float getMaxHealth() {
        return maid.getMaxHealth();
    }

    @Override
    public double getArmorValue() {
        return maid.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
    }

    @Override
    public boolean onHurt() {
        return maid.hurtTime > 0;
    }

    @Override
    public boolean isSleep() {
        return maid.isSleep();
    }

    @Override
    public int getFavorability() {
        return maid.getFavorability();
    }

    @Override
    public boolean isOnGround() {
        return maid.onGround;
    }
}
