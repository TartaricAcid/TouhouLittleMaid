package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class MaidArmorAnimation {
    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/default.js"), getArmorDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/reverse.js"), getArmorReverse());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/temp/cold.js"), getArmorTempCold());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/temp/medium.js"), getArmorTempMedium());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/temp/ocean.js"), getArmorTempOcean());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/temp/warm.js"), getArmorTempWarm());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/value/value_full.js"), getArmorValueFull());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/value/value_high.js"), getArmorValueHigh());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/value/value_low.js"), getArmorValueLow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/value/value_normal.js"), getArmorValueNormal());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/weather/raining.js"), getArmorWeatherRaining());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/weather/thundering.js"), getArmorWeatherThundering());

    }

    public static IAnimation<EntityMaid> getArmorDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmet = modelMap.get("helmet");
                ModelRendererWrapper chestPlate = modelMap.get("chestPlate");
                ModelRendererWrapper chestPlateLeft = modelMap.get("chestPlateLeft");
                ModelRendererWrapper chestPlateMiddle = modelMap.get("chestPlateMiddle");
                ModelRendererWrapper chestPlateRight = modelMap.get("chestPlateRight");
                ModelRendererWrapper leggings = modelMap.get("leggings");
                ModelRendererWrapper leggingsLeft = modelMap.get("leggingsLeft");
                ModelRendererWrapper leggingsMiddle = modelMap.get("leggingsMiddle");
                ModelRendererWrapper leggingsRight = modelMap.get("leggingsRight");
                ModelRendererWrapper bootsLeft = modelMap.get("bootsLeft");
                ModelRendererWrapper bootsRight = modelMap.get("bootsRight");

                if (helmet != null) {
                    helmet.setHidden(!maid.hasHelmet());
                }
                if (chestPlate != null) {
                    chestPlate.setHidden(!maid.hasChestPlate());
                }
                if (chestPlateLeft != null) {
                    chestPlateLeft.setHidden(!maid.hasChestPlate());
                }
                if (chestPlateMiddle != null) {
                    chestPlateMiddle.setHidden(!maid.hasChestPlate());
                }
                if (chestPlateRight != null) {
                    chestPlateRight.setHidden(!maid.hasChestPlate());
                }
                if (leggings != null) {
                    leggings.setHidden(!maid.hasLeggings());
                }
                if (leggingsLeft != null) {
                    leggingsLeft.setHidden(!maid.hasLeggings());
                }
                if (leggingsMiddle != null) {
                    leggingsMiddle.setHidden(!maid.hasLeggings());
                }
                if (leggingsRight != null) {
                    leggingsRight.setHidden(!maid.hasLeggings());
                }
                if (bootsLeft != null) {
                    bootsLeft.setHidden(!maid.hasBoots());
                }
                if (bootsRight != null) {
                    bootsRight.setHidden(!maid.hasBoots());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorReverse() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper reverseHelmet = modelMap.get("_helmet");
                ModelRendererWrapper reverseChestPlate = modelMap.get("_chestPlate");
                ModelRendererWrapper reverseChestPlateLeft = modelMap.get("_chestPlateLeft");
                ModelRendererWrapper reverseChestPlateMiddle = modelMap.get("_chestPlateMiddle");
                ModelRendererWrapper reverseChestPlateRight = modelMap.get("_chestPlateRight");
                ModelRendererWrapper reverseLeggings = modelMap.get("_leggings");
                ModelRendererWrapper reverseLeggingsLeft = modelMap.get("_leggingsLeft");
                ModelRendererWrapper reverseLeggingsMiddle = modelMap.get("_leggingsMiddle");
                ModelRendererWrapper reverseLeggingsRight = modelMap.get("_leggingsRight");
                ModelRendererWrapper reverseBootsLeft = modelMap.get("_bootsLeft");
                ModelRendererWrapper reverseBootsRight = modelMap.get("_bootsRight");

                if (reverseHelmet != null) {
                    reverseHelmet.setHidden(maid.hasHelmet());
                }
                if (reverseChestPlate != null) {
                    reverseChestPlate.setHidden(maid.hasChestPlate());
                }
                if (reverseChestPlateLeft != null) {
                    reverseChestPlateLeft.setHidden(maid.hasChestPlate());
                }
                if (reverseChestPlateMiddle != null) {
                    reverseChestPlateMiddle.setHidden(maid.hasChestPlate());
                }
                if (reverseChestPlateRight != null) {
                    reverseChestPlateRight.setHidden(maid.hasChestPlate());
                }
                if (reverseLeggings != null) {
                    reverseLeggings.setHidden(maid.hasLeggings());
                }
                if (reverseLeggingsLeft != null) {
                    reverseLeggingsLeft.setHidden(maid.hasLeggings());
                }
                if (reverseLeggingsMiddle != null) {
                    reverseLeggingsMiddle.setHidden(maid.hasLeggings());
                }
                if (reverseLeggingsRight != null) {
                    reverseLeggingsRight.setHidden(maid.hasLeggings());
                }
                if (reverseBootsLeft != null) {
                    reverseBootsLeft.setHidden(maid.hasBoots());
                }
                if (reverseBootsRight != null) {
                    reverseBootsRight.setHidden(maid.hasBoots());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorTempCold() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetTempCold = modelMap.get("helmetTempCold");
                ModelRendererWrapper chestPlateTempCold = modelMap.get("chestPlateTempCold");
                ModelRendererWrapper chestPlateLeftTempCold = modelMap.get("chestPlateLeftTempCold");
                ModelRendererWrapper chestPlateMiddleTempCold = modelMap.get("chestPlateMiddleTempCold");
                ModelRendererWrapper chestPlateRightTempCold = modelMap.get("chestPlateRightTempCold");
                ModelRendererWrapper leggingsTempCold = modelMap.get("leggingsTempCold");
                ModelRendererWrapper leggingsLeftTempCold = modelMap.get("leggingsLeftTempCold");
                ModelRendererWrapper leggingsMiddleTempCold = modelMap.get("leggingsMiddleTempCold");
                ModelRendererWrapper leggingsRightTempCold = modelMap.get("leggingsRightTempCold");
                ModelRendererWrapper bootsLeftTempCold = modelMap.get("bootsLeftTempCold");
                ModelRendererWrapper bootsRightTempCold = modelMap.get("bootsRightTempCold");

                boolean tempIsCold = "COLD".equals(maid.getAtBiomeTemp());

                if (helmetTempCold != null) {
                    helmetTempCold.setHidden(!tempIsCold);
                }
                if (chestPlateTempCold != null) {
                    chestPlateTempCold.setHidden(!tempIsCold);
                }
                if (chestPlateLeftTempCold != null) {
                    chestPlateLeftTempCold.setHidden(!tempIsCold);
                }
                if (chestPlateMiddleTempCold != null) {
                    chestPlateMiddleTempCold.setHidden(!tempIsCold);
                }
                if (chestPlateRightTempCold != null) {
                    chestPlateRightTempCold.setHidden(!tempIsCold);
                }
                if (leggingsTempCold != null) {
                    leggingsTempCold.setHidden(!tempIsCold);
                }
                if (leggingsLeftTempCold != null) {
                    leggingsLeftTempCold.setHidden(!tempIsCold);
                }
                if (leggingsMiddleTempCold != null) {
                    leggingsMiddleTempCold.setHidden(!tempIsCold);
                }
                if (leggingsRightTempCold != null) {
                    leggingsRightTempCold.setHidden(!tempIsCold);
                }
                if (bootsLeftTempCold != null) {
                    bootsLeftTempCold.setHidden(!tempIsCold);
                }
                if (bootsRightTempCold != null) {
                    bootsRightTempCold.setHidden(!tempIsCold);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorTempMedium() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetTempMedium = modelMap.get("helmetTempMedium");
                ModelRendererWrapper chestPlateTempMedium = modelMap.get("chestPlateTempMedium");
                ModelRendererWrapper chestPlateLeftTempMedium = modelMap.get("chestPlateLeftTempMedium");
                ModelRendererWrapper chestPlateMiddleTempMedium = modelMap.get("chestPlateMiddleTempMedium");
                ModelRendererWrapper chestPlateRightTempMedium = modelMap.get("chestPlateRightTempMedium");
                ModelRendererWrapper leggingsTempMedium = modelMap.get("leggingsTempMedium");
                ModelRendererWrapper leggingsLeftTempMedium = modelMap.get("leggingsLeftTempMedium");
                ModelRendererWrapper leggingsMiddleTempMedium = modelMap.get("leggingsMiddleTempMedium");
                ModelRendererWrapper leggingsRightTempMedium = modelMap.get("leggingsRightTempMedium");
                ModelRendererWrapper bootsLeftTempMedium = modelMap.get("bootsLeftTempMedium");
                ModelRendererWrapper bootsRightTempMedium = modelMap.get("bootsRightTempMedium");

                boolean tempIsMedium = "MEDIUM".equals(maid.getAtBiomeTemp());

                if (helmetTempMedium != null) {
                    helmetTempMedium.setHidden(!tempIsMedium);
                }
                if (chestPlateTempMedium != null) {
                    chestPlateTempMedium.setHidden(!tempIsMedium);
                }
                if (chestPlateLeftTempMedium != null) {
                    chestPlateLeftTempMedium.setHidden(!tempIsMedium);
                }
                if (chestPlateMiddleTempMedium != null) {
                    chestPlateMiddleTempMedium.setHidden(!tempIsMedium);
                }
                if (chestPlateRightTempMedium != null) {
                    chestPlateRightTempMedium.setHidden(!tempIsMedium);
                }
                if (leggingsTempMedium != null) {
                    leggingsTempMedium.setHidden(!tempIsMedium);
                }
                if (leggingsLeftTempMedium != null) {
                    leggingsLeftTempMedium.setHidden(!tempIsMedium);
                }
                if (leggingsMiddleTempMedium != null) {
                    leggingsMiddleTempMedium.setHidden(!tempIsMedium);
                }
                if (leggingsRightTempMedium != null) {
                    leggingsRightTempMedium.setHidden(!tempIsMedium);
                }
                if (bootsLeftTempMedium != null) {
                    bootsLeftTempMedium.setHidden(!tempIsMedium);
                }
                if (bootsRightTempMedium != null) {
                    bootsRightTempMedium.setHidden(!tempIsMedium);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorTempOcean() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetTempOcean = modelMap.get("helmetTempOcean");
                ModelRendererWrapper chestPlateTempOcean = modelMap.get("chestPlateTempOcean");
                ModelRendererWrapper chestPlateLeftTempOcean = modelMap.get("chestPlateLeftTempOcean");
                ModelRendererWrapper chestPlateMiddleTempOcean = modelMap.get("chestPlateMiddleTempOcean");
                ModelRendererWrapper chestPlateRightTempOcean = modelMap.get("chestPlateRightTempOcean");
                ModelRendererWrapper leggingsTempOcean = modelMap.get("leggingsTempOcean");
                ModelRendererWrapper leggingsLeftTempOcean = modelMap.get("leggingsLeftTempOcean");
                ModelRendererWrapper leggingsMiddleTempOcean = modelMap.get("leggingsMiddleTempOcean");
                ModelRendererWrapper leggingsRightTempOcean = modelMap.get("leggingsRightTempOcean");
                ModelRendererWrapper bootsLeftTempOcean = modelMap.get("bootsLeftTempOcean");
                ModelRendererWrapper bootsRightTempOcean = modelMap.get("bootsRightTempOcean");

                boolean tempIsOcean = "OCEAN".equals(maid.getAtBiomeTemp());

                if (helmetTempOcean != null) {
                    helmetTempOcean.setHidden(!tempIsOcean);
                }
                if (chestPlateTempOcean != null) {
                    chestPlateTempOcean.setHidden(!tempIsOcean);
                }
                if (chestPlateLeftTempOcean != null) {
                    chestPlateLeftTempOcean.setHidden(!tempIsOcean);
                }
                if (chestPlateMiddleTempOcean != null) {
                    chestPlateMiddleTempOcean.setHidden(!tempIsOcean);
                }
                if (chestPlateRightTempOcean != null) {
                    chestPlateRightTempOcean.setHidden(!tempIsOcean);
                }
                if (leggingsTempOcean != null) {
                    leggingsTempOcean.setHidden(!tempIsOcean);
                }
                if (leggingsLeftTempOcean != null) {
                    leggingsLeftTempOcean.setHidden(!tempIsOcean);
                }
                if (leggingsMiddleTempOcean != null) {
                    leggingsMiddleTempOcean.setHidden(!tempIsOcean);
                }
                if (leggingsRightTempOcean != null) {
                    leggingsRightTempOcean.setHidden(!tempIsOcean);
                }
                if (bootsLeftTempOcean != null) {
                    bootsLeftTempOcean.setHidden(!tempIsOcean);
                }
                if (bootsRightTempOcean != null) {
                    bootsRightTempOcean.setHidden(!tempIsOcean);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorTempWarm() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetTempWarm = modelMap.get("helmetTempWarm");
                ModelRendererWrapper chestPlateTempWarm = modelMap.get("chestPlateTempWarm");
                ModelRendererWrapper chestPlateLeftTempWarm = modelMap.get("chestPlateLeftTempWarm");
                ModelRendererWrapper chestPlateMiddleTempWarm = modelMap.get("chestPlateMiddleTempWarm");
                ModelRendererWrapper chestPlateRightTempWarm = modelMap.get("chestPlateRightTempWarm");
                ModelRendererWrapper leggingsTempWarm = modelMap.get("leggingsTempWarm");
                ModelRendererWrapper leggingsLeftTempWarm = modelMap.get("leggingsLeftTempWarm");
                ModelRendererWrapper leggingsMiddleTempWarm = modelMap.get("leggingsMiddleTempWarm");
                ModelRendererWrapper leggingsRightTempWarm = modelMap.get("leggingsRightTempWarm");
                ModelRendererWrapper bootsLeftTempWarm = modelMap.get("bootsLeftTempWarm");
                ModelRendererWrapper bootsRightTempWarm = modelMap.get("bootsRightTempWarm");

                boolean tempIsWarm = "WARM".equals(maid.getAtBiomeTemp());

                if (helmetTempWarm != null) {
                    helmetTempWarm.setHidden(!tempIsWarm);
                }
                if (chestPlateTempWarm != null) {
                    chestPlateTempWarm.setHidden(!tempIsWarm);
                }
                if (chestPlateLeftTempWarm != null) {
                    chestPlateLeftTempWarm.setHidden(!tempIsWarm);
                }
                if (chestPlateMiddleTempWarm != null) {
                    chestPlateMiddleTempWarm.setHidden(!tempIsWarm);
                }
                if (chestPlateRightTempWarm != null) {
                    chestPlateRightTempWarm.setHidden(!tempIsWarm);
                }
                if (leggingsTempWarm != null) {
                    leggingsTempWarm.setHidden(!tempIsWarm);
                }
                if (leggingsLeftTempWarm != null) {
                    leggingsLeftTempWarm.setHidden(!tempIsWarm);
                }
                if (leggingsMiddleTempWarm != null) {
                    leggingsMiddleTempWarm.setHidden(!tempIsWarm);
                }
                if (leggingsRightTempWarm != null) {
                    leggingsRightTempWarm.setHidden(!tempIsWarm);
                }
                if (bootsLeftTempWarm != null) {
                    bootsLeftTempWarm.setHidden(!tempIsWarm);
                }
                if (bootsRightTempWarm != null) {
                    bootsRightTempWarm.setHidden(!tempIsWarm);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorValueFull() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetValueFull = modelMap.get("helmetValueFull");
                ModelRendererWrapper chestPlateValueFull = modelMap.get("chestPlateValueFull");
                ModelRendererWrapper chestPlateLeftValueFull = modelMap.get("chestPlateLeftValueFull");
                ModelRendererWrapper chestPlateMiddleValueFull = modelMap.get("chestPlateMiddleValueFull");
                ModelRendererWrapper chestPlateRightValueFull = modelMap.get("chestPlateRightValueFull");
                ModelRendererWrapper leggingsValueFull = modelMap.get("leggingsValueFull");
                ModelRendererWrapper leggingsLeftValueFull = modelMap.get("leggingsLeftValueFull");
                ModelRendererWrapper leggingsMiddleValueFull = modelMap.get("leggingsMiddleValueFull");
                ModelRendererWrapper leggingsRightValueFull = modelMap.get("leggingsRightValueFull");
                ModelRendererWrapper bootsLeftValueFull = modelMap.get("bootsLeftValueFull");
                ModelRendererWrapper bootsRightValueFull = modelMap.get("bootsRightValueFull");

                boolean valueIsFull = 15 < maid.getArmorValue();

                if (helmetValueFull != null) {
                    helmetValueFull.setHidden(!valueIsFull);
                }
                if (chestPlateValueFull != null) {
                    chestPlateValueFull.setHidden(!valueIsFull);
                }
                if (chestPlateLeftValueFull != null) {
                    chestPlateLeftValueFull.setHidden(!valueIsFull);
                }
                if (chestPlateMiddleValueFull != null) {
                    chestPlateMiddleValueFull.setHidden(!valueIsFull);
                }
                if (chestPlateRightValueFull != null) {
                    chestPlateRightValueFull.setHidden(!valueIsFull);
                }
                if (leggingsValueFull != null) {
                    leggingsValueFull.setHidden(!valueIsFull);
                }
                if (leggingsLeftValueFull != null) {
                    leggingsLeftValueFull.setHidden(!valueIsFull);
                }
                if (leggingsMiddleValueFull != null) {
                    leggingsMiddleValueFull.setHidden(!valueIsFull);
                }
                if (leggingsRightValueFull != null) {
                    leggingsRightValueFull.setHidden(!valueIsFull);
                }
                if (bootsLeftValueFull != null) {
                    bootsLeftValueFull.setHidden(!valueIsFull);
                }
                if (bootsRightValueFull != null) {
                    bootsRightValueFull.setHidden(!valueIsFull);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorValueHigh() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetValueHigh = modelMap.get("helmetValueHigh");
                ModelRendererWrapper chestPlateValueHigh = modelMap.get("chestPlateValueHigh");
                ModelRendererWrapper chestPlateLeftValueHigh = modelMap.get("chestPlateLeftValueHigh");
                ModelRendererWrapper chestPlateMiddleValueHigh = modelMap.get("chestPlateMiddleValueHigh");
                ModelRendererWrapper chestPlateRightValueHigh = modelMap.get("chestPlateRightValueHigh");
                ModelRendererWrapper leggingsValueHigh = modelMap.get("leggingsValueHigh");
                ModelRendererWrapper leggingsLeftValueHigh = modelMap.get("leggingsLeftValueHigh");
                ModelRendererWrapper leggingsMiddleValueHigh = modelMap.get("leggingsMiddleValueHigh");
                ModelRendererWrapper leggingsRightValueHigh = modelMap.get("leggingsRightValueHigh");
                ModelRendererWrapper bootsLeftValueHigh = modelMap.get("bootsLeftValueHigh");
                ModelRendererWrapper bootsRightValueHigh = modelMap.get("bootsRightValueHigh");

                boolean valueIsHigh = (10 < maid.getArmorValue()) && (maid.getArmorValue() <= 15);

                if (helmetValueHigh != null) {
                    helmetValueHigh.setHidden(!valueIsHigh);
                }
                if (chestPlateValueHigh != null) {
                    chestPlateValueHigh.setHidden(!valueIsHigh);
                }
                if (chestPlateLeftValueHigh != null) {
                    chestPlateLeftValueHigh.setHidden(!valueIsHigh);
                }
                if (chestPlateMiddleValueHigh != null) {
                    chestPlateMiddleValueHigh.setHidden(!valueIsHigh);
                }
                if (chestPlateRightValueHigh != null) {
                    chestPlateRightValueHigh.setHidden(!valueIsHigh);
                }
                if (leggingsValueHigh != null) {
                    leggingsValueHigh.setHidden(!valueIsHigh);
                }
                if (leggingsLeftValueHigh != null) {
                    leggingsLeftValueHigh.setHidden(!valueIsHigh);
                }
                if (leggingsMiddleValueHigh != null) {
                    leggingsMiddleValueHigh.setHidden(!valueIsHigh);
                }
                if (leggingsRightValueHigh != null) {
                    leggingsRightValueHigh.setHidden(!valueIsHigh);
                }
                if (bootsLeftValueHigh != null) {
                    bootsLeftValueHigh.setHidden(!valueIsHigh);
                }
                if (bootsRightValueHigh != null) {
                    bootsRightValueHigh.setHidden(!valueIsHigh);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorValueLow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetValueLow = modelMap.get("helmetValueLow");
                ModelRendererWrapper chestPlateValueLow = modelMap.get("chestPlateValueLow");
                ModelRendererWrapper chestPlateLeftValueLow = modelMap.get("chestPlateLeftValueLow");
                ModelRendererWrapper chestPlateMiddleValueLow = modelMap.get("chestPlateMiddleValueLow");
                ModelRendererWrapper chestPlateRightValueLow = modelMap.get("chestPlateRightValueLow");
                ModelRendererWrapper leggingsValueLow = modelMap.get("leggingsValueLow");
                ModelRendererWrapper leggingsLeftValueLow = modelMap.get("leggingsLeftValueLow");
                ModelRendererWrapper leggingsMiddleValueLow = modelMap.get("leggingsMiddleValueLow");
                ModelRendererWrapper leggingsRightValueLow = modelMap.get("leggingsRightValueLow");
                ModelRendererWrapper bootsLeftValueLow = modelMap.get("bootsLeftValueLow");
                ModelRendererWrapper bootsRightValueLow = modelMap.get("bootsRightValueLow");

                boolean valueIsLow = (0 < maid.getArmorValue()) && (maid.getArmorValue() <= 5);

                if (helmetValueLow != null) {
                    helmetValueLow.setHidden(!valueIsLow);
                }
                if (chestPlateValueLow != null) {
                    chestPlateValueLow.setHidden(!valueIsLow);
                }
                if (chestPlateLeftValueLow != null) {
                    chestPlateLeftValueLow.setHidden(!valueIsLow);
                }
                if (chestPlateMiddleValueLow != null) {
                    chestPlateMiddleValueLow.setHidden(!valueIsLow);
                }
                if (chestPlateRightValueLow != null) {
                    chestPlateRightValueLow.setHidden(!valueIsLow);
                }
                if (leggingsValueLow != null) {
                    leggingsValueLow.setHidden(!valueIsLow);
                }
                if (leggingsLeftValueLow != null) {
                    leggingsLeftValueLow.setHidden(!valueIsLow);
                }
                if (leggingsMiddleValueLow != null) {
                    leggingsMiddleValueLow.setHidden(!valueIsLow);
                }
                if (leggingsRightValueLow != null) {
                    leggingsRightValueLow.setHidden(!valueIsLow);
                }
                if (bootsLeftValueLow != null) {
                    bootsLeftValueLow.setHidden(!valueIsLow);
                }
                if (bootsRightValueLow != null) {
                    bootsRightValueLow.setHidden(!valueIsLow);
                }
            }
        };
    }


    public static IAnimation<EntityMaid> getArmorValueNormal() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetValueNormal = modelMap.get("helmetValueNormal");
                ModelRendererWrapper chestPlateValueNormal = modelMap.get("chestPlateValueNormal");
                ModelRendererWrapper chestPlateLeftValueNormal = modelMap.get("chestPlateLeftValueNormal");
                ModelRendererWrapper chestPlateMiddleValueNormal = modelMap.get("chestPlateMiddleValueNormal");
                ModelRendererWrapper chestPlateRightValueNormal = modelMap.get("chestPlateRightValueNormal");
                ModelRendererWrapper leggingsValueNormal = modelMap.get("leggingsValueNormal");
                ModelRendererWrapper leggingsLeftValueNormal = modelMap.get("leggingsLeftValueNormal");
                ModelRendererWrapper leggingsMiddleValueNormal = modelMap.get("leggingsMiddleValueNormal");
                ModelRendererWrapper leggingsRightValueNormal = modelMap.get("leggingsRightValueNormal");
                ModelRendererWrapper bootsLeftValueNormal = modelMap.get("bootsLeftValueNormal");
                ModelRendererWrapper bootsRightValueNormal = modelMap.get("bootsRightValueNormal");

                boolean valueIsNormal = (5 < maid.getArmorValue()) && (maid.getArmorValue() <= 10);

                if (helmetValueNormal != null) {
                    helmetValueNormal.setHidden(!valueIsNormal);
                }
                if (chestPlateValueNormal != null) {
                    chestPlateValueNormal.setHidden(!valueIsNormal);
                }
                if (chestPlateLeftValueNormal != null) {
                    chestPlateLeftValueNormal.setHidden(!valueIsNormal);
                }
                if (chestPlateMiddleValueNormal != null) {
                    chestPlateMiddleValueNormal.setHidden(!valueIsNormal);
                }
                if (chestPlateRightValueNormal != null) {
                    chestPlateRightValueNormal.setHidden(!valueIsNormal);
                }
                if (leggingsValueNormal != null) {
                    leggingsValueNormal.setHidden(!valueIsNormal);
                }
                if (leggingsLeftValueNormal != null) {
                    leggingsLeftValueNormal.setHidden(!valueIsNormal);
                }
                if (leggingsMiddleValueNormal != null) {
                    leggingsMiddleValueNormal.setHidden(!valueIsNormal);
                }
                if (leggingsRightValueNormal != null) {
                    leggingsRightValueNormal.setHidden(!valueIsNormal);
                }
                if (bootsLeftValueNormal != null) {
                    bootsLeftValueNormal.setHidden(!valueIsNormal);
                }
                if (bootsRightValueNormal != null) {
                    bootsRightValueNormal.setHidden(!valueIsNormal);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmorWeatherRaining() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetWeatherRaining = modelMap.get("helmetWeatherRaining");
                ModelRendererWrapper chestPlateWeatherRaining = modelMap.get("chestPlateWeatherRaining");
                ModelRendererWrapper chestPlateLeftWeatherRaining = modelMap.get("chestPlateLeftWeatherRaining");
                ModelRendererWrapper chestPlateMiddleWeatherRaining = modelMap.get("chestPlateMiddleWeatherRaining");
                ModelRendererWrapper chestPlateRightWeatherRaining = modelMap.get("chestPlateRightWeatherRaining");
                ModelRendererWrapper leggingsWeatherRaining = modelMap.get("leggingsWeatherRaining");
                ModelRendererWrapper leggingsLeftWeatherRaining = modelMap.get("leggingsLeftWeatherRaining");
                ModelRendererWrapper leggingsMiddleWeatherRaining = modelMap.get("leggingsMiddleWeatherRaining");
                ModelRendererWrapper leggingsRightWeatherRaining = modelMap.get("leggingsRightWeatherRaining");
                ModelRendererWrapper bootsLeftWeatherRaining = modelMap.get("bootsLeftWeatherRaining");
                ModelRendererWrapper bootsRightWeatherRaining = modelMap.get("bootsRightWeatherRaining");

                boolean weatherIsRaining = maid.level.isRaining();

                if (helmetWeatherRaining != null) {
                    helmetWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (chestPlateWeatherRaining != null) {
                    chestPlateWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (chestPlateLeftWeatherRaining != null) {
                    chestPlateLeftWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (chestPlateMiddleWeatherRaining != null) {
                    chestPlateMiddleWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (chestPlateRightWeatherRaining != null) {
                    chestPlateRightWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (leggingsWeatherRaining != null) {
                    leggingsWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (leggingsLeftWeatherRaining != null) {
                    leggingsLeftWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (leggingsMiddleWeatherRaining != null) {
                    leggingsMiddleWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (leggingsRightWeatherRaining != null) {
                    leggingsRightWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (bootsLeftWeatherRaining != null) {
                    bootsLeftWeatherRaining.setHidden(!weatherIsRaining);
                }
                if (bootsRightWeatherRaining != null) {
                    bootsRightWeatherRaining.setHidden(!weatherIsRaining);
                }
            }
        };
    }


    public static IAnimation<EntityMaid> getArmorWeatherThundering() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetWeatherThundering = modelMap.get("helmetWeatherThundering");
                ModelRendererWrapper chestPlateWeatherThundering = modelMap.get("chestPlateWeatherThundering");
                ModelRendererWrapper chestPlateLeftWeatherThundering = modelMap.get("chestPlateLeftWeatherThundering");
                ModelRendererWrapper chestPlateMiddleWeatherThundering = modelMap.get("chestPlateMiddleWeatherThundering");
                ModelRendererWrapper chestPlateRightWeatherThundering = modelMap.get("chestPlateRightWeatherThundering");
                ModelRendererWrapper leggingsWeatherThundering = modelMap.get("leggingsWeatherThundering");
                ModelRendererWrapper leggingsLeftWeatherThundering = modelMap.get("leggingsLeftWeatherThundering");
                ModelRendererWrapper leggingsMiddleWeatherThundering = modelMap.get("leggingsMiddleWeatherThundering");
                ModelRendererWrapper leggingsRightWeatherThundering = modelMap.get("leggingsRightWeatherThundering");
                ModelRendererWrapper bootsLeftWeatherThundering = modelMap.get("bootsLeftWeatherThundering");
                ModelRendererWrapper bootsRightWeatherThundering = modelMap.get("bootsRightWeatherThundering");

                boolean weatherIsThundering = maid.level.isThundering();

                if (helmetWeatherThundering != null) {
                    helmetWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (chestPlateWeatherThundering != null) {
                    chestPlateWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (chestPlateLeftWeatherThundering != null) {
                    chestPlateLeftWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (chestPlateMiddleWeatherThundering != null) {
                    chestPlateMiddleWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (chestPlateRightWeatherThundering != null) {
                    chestPlateRightWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (leggingsWeatherThundering != null) {
                    leggingsWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (leggingsLeftWeatherThundering != null) {
                    leggingsLeftWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (leggingsMiddleWeatherThundering != null) {
                    leggingsMiddleWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (leggingsRightWeatherThundering != null) {
                    leggingsRightWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (bootsLeftWeatherThundering != null) {
                    bootsLeftWeatherThundering.setHidden(!weatherIsThundering);
                }
                if (bootsRightWeatherThundering != null) {
                    bootsRightWeatherThundering.setHidden(!weatherIsThundering);
                }
            }
        };
    }
}
