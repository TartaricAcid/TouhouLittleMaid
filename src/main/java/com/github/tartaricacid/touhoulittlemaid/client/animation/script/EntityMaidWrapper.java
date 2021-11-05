package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

public final class EntityMaidWrapper implements IMaidData {
    private final WorldWrapper world = new WorldWrapper();
    private final float[] handRotation = new float[]{0, 0, 0};
    private float swingProgress;
    private boolean isRiding;
    private EntityMaid maid;

    public void setData(EntityMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
        this.world.setData(maid.level);
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
        return maid.getTask().getUid().toString();
    }

    @Override
    public boolean hasHelmet() {
        return !maid.getItemBySlot(EquipmentSlotType.HEAD).isEmpty();
    }

    @Override
    public String getHelmet() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.HEAD).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasChestPlate() {
        return !maid.getItemBySlot(EquipmentSlotType.CHEST).isEmpty();
    }

    @Override
    public String getChestPlate() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.CHEST).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasLeggings() {
        return !maid.getItemBySlot(EquipmentSlotType.LEGS).isEmpty();
    }

    @Override
    public String getLeggings() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.LEGS).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasBoots() {
        return !maid.getItemBySlot(EquipmentSlotType.FEET).isEmpty();
    }

    @Override
    public String getBoots() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.FEET).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemMainhand() {
        return maid.getMainHandItem().isEmpty();
    }

    @Override
    public String getItemMainhand() {
        ResourceLocation res = maid.getMainHandItem().getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemOffhand() {
        return maid.getOffhandItem().isEmpty();
    }

    @Override
    public String getItemOffhand() {
        ResourceLocation res = maid.getOffhandItem().getItem().getRegistryName();
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
        return maid.isInSittingPose();
    }

    @Override
    public boolean hasBackpack() {
        return maid.hasBackpack();
    }

    @Override
    public int getBackpackLevel() {
        return maid.getBackpackLevel();
    }

    @Override
    public boolean inWater() {
        return maid.isInWater();
    }

    @Override
    public boolean inRain() {
        return maid.level.isRainingAt(maid.blockPosition());
    }

    @Override
    public boolean isSwingLeftHand() {
        return maid.swingingArm == Hand.OFF_HAND;
    }

    @Override
    public float getSwingProgress() {
        return swingProgress;
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
        return maid.getAttributeValue(Attributes.ARMOR);
    }

    @Override
    public boolean onHurt() {
        return maid.hurtTime > 0;
    }

    @Override
    public boolean isSleep() {
        return maid.isSleeping();
    }

    @Override
    public int getFavorability() {
        return maid.getFavorability();
    }

    @Override
    public long getSeed() {
        return Math.abs(maid.getUUID().getLeastSignificantBits());
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
    public boolean isOnGround() {
        return !maid.isOnGround();
    }

    @Override
    @Deprecated
    public String getAtBiomeTemp() {
        return maid.getAtBiomeTemp();
    }

    @Override
    @Deprecated
    public boolean hasSasimono() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isHoldTrolley() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isRidingMarisaBroom() {
        return false;
    }

    @Override
    public boolean isRidingPlayer() {
        return maid.getVehicle() instanceof PlayerEntity;
    }

    @Override
    @Deprecated
    public boolean isHoldVehicle() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isPortableAudioPlay() {
        return false;
    }

    @Override
    @Deprecated
    public float[] getLeftHandRotation() {
        return handRotation;
    }

    @Override
    @Deprecated
    public float[] getRightHandRotation() {
        return handRotation;
    }

    @Override
    @Deprecated
    public int getDim() {
        return maid.getDim();
    }
}
