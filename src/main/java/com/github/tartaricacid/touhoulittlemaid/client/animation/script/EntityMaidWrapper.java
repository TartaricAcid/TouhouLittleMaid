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
        // FIXME：需要修改
        return "";
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
        // FIXME：需要更改
        return false;
    }

    public boolean isSwingingArms() {
        // FIXME：需要更改
        return false;
    }

    public boolean isRiding() {
        return isRiding;
    }

    public boolean isSitting() {
        return maid.isInSittingPose();
    }

    public boolean hasBackpack() {
        // FIXME：需要更改
        return false;
    }

    public int getBackpackLevel() {
        // FIXME：需要更改
        return 0;
    }

    public boolean hasSasimono() {
        // FIXME：需要更改
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

    public String getAtBiomeTemp() {
        // FIXME:待修正
        return "";
    }

    public boolean isHoldTrolley() {
        // FIXME：需要更改
        return false;
    }

    /**
     * 兼容旧版本动画，现已不用
     */
    @Deprecated
    public boolean isRidingMarisaBroom() {
        return false;
    }

    public boolean isRidingPlayer() {
        return maid.getVehicle() instanceof PlayerEntity;
    }

    public boolean isHoldVehicle() {
        // FIXME：需要更改
        return false;
    }

    /**
     * 兼容旧版本动画，现已不用
     */
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

    public float[] getLeftHandRotation() {
        // FIXME：需要更改
        return new float[]{0, 0, 0};
    }

    public float[] getRightHandRotation() {
        // FIXME：需要更改
        return new float[]{0, 0, 0};
    }

    public int getDim() {
        // FIXME：需要更改
        return 0;
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
        return maid.isSleep();
    }

    public int getFavorability() {
        // FIXME：需要更改
        return 0;
    }
}
