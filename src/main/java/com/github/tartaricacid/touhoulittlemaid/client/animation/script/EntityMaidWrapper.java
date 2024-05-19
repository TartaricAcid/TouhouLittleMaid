package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

public final class EntityMaidWrapper implements IMaidData {
    private final WorldWrapper world = new WorldWrapper();
    private final float[] handRotation = new float[]{0, 0, 0};
    private float swingProgress;
    private boolean isRiding;
    private IMaid maid;

    public void setData(IMaid maid, float swingProgress, boolean isRiding) {
        this.maid = maid;
        this.swingProgress = swingProgress;
        this.isRiding = isRiding;
        this.world.setData(maid.asEntity().level());
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
        return maid.hasHelmet();
    }

    @Override
    public String getHelmet() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getItemBySlot(EquipmentSlot.HEAD).getItem());
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasChestPlate() {
        return maid.hasChestPlate();
    }

    @Override
    public String getChestPlate() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getItemBySlot(EquipmentSlot.CHEST).getItem());
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasLeggings() {
        return maid.hasLeggings();
    }

    @Override
    public String getLeggings() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getItemBySlot(EquipmentSlot.LEGS).getItem());
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasBoots() {
        return maid.hasBoots();
    }

    @Override
    public String getBoots() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getItemBySlot(EquipmentSlot.FEET).getItem());
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemMainhand() {
        return maid.asEntity().getMainHandItem().isEmpty();
    }

    @Override
    public String getItemMainhand() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getMainHandItem().getItem());
        if (res != null) {
            return res.toString();
        }
        return "";
    }

    @Override
    public boolean hasItemOffhand() {
        return maid.asEntity().getOffhandItem().isEmpty();
    }

    @Override
    public String getItemOffhand() {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(maid.asEntity().getOffhandItem().getItem());
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
    @Deprecated
    public int getBackpackLevel() {
        return 0;
    }

    @Override
    public boolean inWater() {
        return maid.asEntity().isInWater();
    }

    @Override
    public boolean inRain() {
        return maid.asEntity().level().isRainingAt(maid.asEntity().blockPosition());
    }

    @Override
    public boolean isSwingLeftHand() {
        return maid.asEntity().swingingArm == InteractionHand.OFF_HAND;
    }

    @Override
    public float getSwingProgress() {
        return swingProgress;
    }

    @Override
    public float getHealth() {
        return maid.asEntity().getHealth();
    }

    @Override
    public float getMaxHealth() {
        return maid.asEntity().getMaxHealth();
    }

    @Override
    public double getArmorValue() {
        return maid.asEntity().getAttributeValue(Attributes.ARMOR);
    }

    @Override
    public boolean onHurt() {
        return maid.asEntity().hurtTime > 0;
    }

    @Override
    public boolean isSleep() {
        return maid.asEntity().isSleeping();
    }

    @Override
    public int getFavorability() {
        return maid.getFavorability();
    }

    @Override
    public long getSeed() {
        return Math.abs(maid.asEntity().getUUID().getLeastSignificantBits());
    }

    @Override
    public String getAtBiome() {
        ResourceLocation res = ForgeRegistries.BIOMES.getKey(BiomeCacheUtil.getCacheBiome(maid.asEntity()));
        if (res != null) {
            return res.getPath();
        }
        return "";
    }

    @Override
    public boolean isOnGround() {
        return !maid.asEntity().onGround();
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
        return maid.asEntity().getVehicle() instanceof Player;
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
