package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class EntityMaidWrapper {
    public float swingProgress;
    public boolean isRiding;
    private EntityMaid maid;
    private WorldWrapper world;
    private Biome biome;

    public void setData(EntityMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
        this.world = new WorldWrapper(maid.level);
        this.biome = maid.level.getBiome(maid.blockPosition());
    }

    public void clearData() {
        this.maid = null;
        this.world = null;
        this.biome = null;
    }

    public WorldWrapper getWorld() {
        return world;
    }

    public String getTask() {
        return maid.getTask().getUid().toString();
    }

    public boolean hasHelmet() {
        return !maid.getItemBySlot(EquipmentSlotType.HEAD).isEmpty();
    }

    public String getHelmet() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.HEAD).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    public boolean hasChestPlate() {
        return !maid.getItemBySlot(EquipmentSlotType.CHEST).isEmpty();
    }

    public String getChestPlate() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.CHEST).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    public boolean hasLeggings() {
        return !maid.getItemBySlot(EquipmentSlotType.LEGS).isEmpty();
    }

    public String getLeggings() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.LEGS).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    public boolean hasBoots() {
        return !maid.getItemBySlot(EquipmentSlotType.FEET).isEmpty();
    }

    public String getBoots() {
        ResourceLocation res = maid.getItemBySlot(EquipmentSlotType.FEET).getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    public boolean hasItemMainhand() {
        return maid.getMainHandItem().isEmpty();
    }

    public String getItemMainhand() {
        ResourceLocation res = maid.getMainHandItem().getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    public boolean hasItemOffhand() {
        return maid.getOffhandItem().isEmpty();
    }

    public String getItemOffhand() {
        ResourceLocation res = maid.getOffhandItem().getItem().getRegistryName();
        if (res != null) {
            return res.toString();
        }
        return "";
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
        return maid.isInSittingPose();
    }

    public boolean hasBackpack() {
        return maid.hasBackpack();
    }

    public int getBackpackLevel() {
        return maid.getBackpackLevel();
    }

    @Deprecated
    public boolean hasSasimono() {
        return false;
    }

    public boolean inWater() {
        return maid.isInWater();
    }

    public boolean inRain() {
        return maid.level.isRainingAt(maid.blockPosition());
    }

    public String getAtBiome() {
        ResourceLocation res = biome.getRegistryName();
        if (res != null) {
            return res.getPath();
        }
        return "";
    }

    @Deprecated
    public String getAtBiomeTemp() {
        return maid.getAtBiomeTemp();
    }

    @Deprecated
    public boolean isHoldTrolley() {
        return false;
    }

    @Deprecated
    public boolean isRidingMarisaBroom() {
        return false;
    }

    public boolean isRidingPlayer() {
        return maid.getVehicle() instanceof PlayerEntity;
    }

    @Deprecated
    public boolean isHoldVehicle() {
        return false;
    }

    @Deprecated
    public boolean isPortableAudioPlay() {
        return false;
    }

    public boolean isSwingLeftHand() {
        return maid.swingingArm == Hand.OFF_HAND;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    @Deprecated
    public float[] getLeftHandRotation() {
        return new float[]{0, 0, 0};
    }

    @Deprecated
    public float[] getRightHandRotation() {
        return new float[]{0, 0, 0};
    }

    @Deprecated
    public int getDim() {
        return maid.getDim();
    }

    public float getHealth() {
        return maid.getHealth();
    }

    public float getMaxHealth() {
        return maid.getMaxHealth();
    }

    public double getArmorValue() {
        return maid.getAttributeValue(Attributes.ARMOR);
    }

    public boolean onHurt() {
        return maid.hurtTime > 0;
    }

    public boolean isSleep() {
        return maid.isSleeping();
    }

    public int getFavorability() {
        return maid.getFavorability();
    }
}
