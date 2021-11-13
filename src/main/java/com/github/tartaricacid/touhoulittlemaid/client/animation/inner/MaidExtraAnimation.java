package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class MaidExtraAnimation {
    private static final float[] FIXED_HAND_ROTATION = new float[]{0, 0, 0};

    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/extra.js"), getArmExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/vertical.js"), getArmVertical());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/extra.js"), getHeadExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/hurt.js"), getHeadHurt());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/reverse_blink.js"), getHeadReverseBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/extra.js"), getLegExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/vertical.js"), getLegVertical());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/less_show.js"), getHealthLessShow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/more_show.js"), getHealthMoreShow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/rotation.js"), getHealthRotation());
    }

    public static IAnimation<EntityMaid> getArmExtra() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeft = modelMap.get("armLeftExtraA");
                ModelRendererWrapper armRight = modelMap.get("armRightExtraA");

                double f1 = 1.0 - Math.pow(1.0 - maid.attackAnim, 4);
                double f2 = Math.sin(f1 * Math.PI);
                double f3 = Math.sin(maid.attackAnim * Math.PI) * -0.7 * 0.75;

                float[] rotation;
                if (armLeft != null) {
                    if (isHoldTrolley()) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle()) {
                        rotation = getLeftHandRotation();
                        armLeft.setRotateAngleX(rotation[0]);
                        armLeft.setRotateAngleY(rotation[1]);
                        armLeft.setRotateAngleZ(rotation[2]);
                    } else {
                        armLeft.setRotateAngleX((float) (-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount));
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.05) * 0.05 - 0.4));
                        // 手部使用动画
                        if (maid.attackAnim > 0.0 && isSwingLeftHand(maid)) {
                            armLeft.setRotateAngleX((float) (armLeft.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armLeft.setRotateAngleZ((float) (armLeft.getRotateAngleZ() + Math.sin(maid.attackAnim * Math.PI) * -0.4));
                        }
                    }
                }

                if (armRight != null) {
                    if (isHoldVehicle()) {
                        rotation = getRightHandRotation();
                        armRight.setRotateAngleX(rotation[0]);
                        armRight.setRotateAngleY(rotation[1]);
                        armRight.setRotateAngleZ(rotation[2]);
                    } else {
                        armRight.setRotateAngleX((float) (Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount));
                        armRight.setRotateAngleY(0);
                        armRight.setRotateAngleZ((float) (-Math.cos(ageInTicks * 0.05) * 0.05 + 0.4));
                        // 手部使用动画
                        if (maid.attackAnim > 0.0 && !isSwingLeftHand(maid)) {
                            armRight.setRotateAngleX((float) (armRight.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armRight.setRotateAngleZ((float) (armRight.getRotateAngleZ() + Math.sin(maid.attackAnim * Math.PI) * -0.4));
                        }
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getArmVertical() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeftVertical = modelMap.get("armLeftVertical");
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                if (armLeftVertical != null) {
                    if (armLeft != null) {
                        armLeftVertical.setRotateAngleX(-armLeft.getRotateAngleX());
                        armLeftVertical.setRotateAngleZ(-armLeft.getRotateAngleZ());
                    }
                }

                ModelRendererWrapper armRightVertical = modelMap.get("armRightVertical");
                ModelRendererWrapper armRight = modelMap.get("armRight");
                if (armRightVertical != null) {
                    if (armRight != null) {
                        armRightVertical.setRotateAngleX(-armRight.getRotateAngleX());
                        armRightVertical.setRotateAngleZ(-armRight.getRotateAngleZ());
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHeadExtra() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper headExtraA = modelMap.get("headExtraA");
                if (headExtraA != null) {
                    headExtraA.setRotateAngleX(headPitch * 0.017453292f);
                    headExtraA.setRotateAngleY(netHeadYaw * 0.017453292f);
                }

                ModelRendererWrapper headExtraB = modelMap.get("headExtraB");
                if (headExtraB != null) {
                    headExtraB.setRotateAngleX(headPitch * 0.017453292f);
                    headExtraB.setRotateAngleY(netHeadYaw * 0.017453292f);
                }

                ModelRendererWrapper headExtraC = modelMap.get("headExtraC");
                if (headExtraC != null) {
                    headExtraC.setRotateAngleX(headPitch * 0.017453292f);
                    headExtraC.setRotateAngleY(netHeadYaw * 0.017453292f);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHeadHurt() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper hurtBlink = modelMap.get("hurtBlink");

                if (hurtBlink != null) {
                    hurtBlink.setHidden(!maid.onHurt());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHeadReverseBlink() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                // The previous typo is reserved for compatibility
                ModelRendererWrapper reverseBlink = modelMap.get("_bink");
                ModelRendererWrapper reverseBlinkCorrect = modelMap.get("_blink");
                if (reverseBlink != null) {
                    float remainder = (ageInTicks + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 60;
                    reverseBlink.setHidden(55 < remainder && remainder < 60);
                    reverseBlinkCorrect.setHidden(55 < remainder && remainder < 60);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getLegExtra() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper legLeftExtraA = modelMap.get("legLeftExtraA");
                ModelRendererWrapper legRightExtraA = modelMap.get("legRightExtraA");

                if (legLeftExtraA != null) {
                    legLeftExtraA.setRotateAngleX((float) (Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount));
                    legLeftExtraA.setRotateAngleY(0);
                    legLeftExtraA.setRotateAngleZ(0);
                }
                if (legRightExtraA != null) {
                    legRightExtraA.setRotateAngleX((float) (-Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount));
                    legRightExtraA.setRotateAngleY(0);
                    legRightExtraA.setRotateAngleZ(0);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getLegVertical() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper legLeftVertical = modelMap.get("legLeftVertical");
                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                if (legLeftVertical != null) {
                    if (legLeft != null) {
                        legLeftVertical.setRotateAngleX(-legLeft.getRotateAngleX());
                        legLeftVertical.setRotateAngleZ(-legLeft.getRotateAngleZ());
                    }
                }

                ModelRendererWrapper legRightVertical = modelMap.get("legRightVertical");
                ModelRendererWrapper legRight = modelMap.get("legRight");
                if (legRightVertical != null) {
                    if (legRight != null) {
                        legRightVertical.setRotateAngleX(-legRight.getRotateAngleX());
                        legRightVertical.setRotateAngleZ(-legRight.getRotateAngleZ());
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHealthLessShow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper healthLessQuarterShow = modelMap.get("healthLessQuarterShow");
                ModelRendererWrapper healthLessHalfShow = modelMap.get("healthLessHalfShow");
                ModelRendererWrapper healthLessThreeQuartersShow = modelMap.get("healthLessThreeQuartersShow");

                double ratio = maid.getHealth() / maid.getMaxHealth();

                if (healthLessQuarterShow != null) {
                    healthLessQuarterShow.setHidden(ratio > 0.25);
                }

                if (healthLessHalfShow != null) {
                    healthLessHalfShow.setHidden(ratio > 0.5);
                }

                if (healthLessThreeQuartersShow != null) {
                    healthLessThreeQuartersShow.setHidden(ratio > 0.75);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHealthMoreShow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper healthMoreQuarterShow = modelMap.get("healthMoreQuarterShow");
                ModelRendererWrapper healthMoreHalfShow = modelMap.get("healthMoreHalfShow");
                ModelRendererWrapper healthMoreThreeQuartersShow = modelMap.get("healthMoreThreeQuartersShow");

                double ratio = maid.getHealth() / maid.getMaxHealth();

                if (healthMoreQuarterShow != null) {
                    healthMoreQuarterShow.setHidden(ratio <= 0.25);
                }

                if (healthMoreHalfShow != null) {
                    healthMoreHalfShow.setHidden(ratio <= 0.5);
                }

                if (healthMoreThreeQuartersShow != null) {
                    healthMoreThreeQuartersShow.setHidden(ratio <= 0.75);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHealthRotation() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper healthRotationX90 = modelMap.get("healthRotationX90");

                if (healthRotationX90 != null) {
                    double deg = (Math.PI / 4) - (Math.PI / 2) * (maid.getHealth() / maid.getMaxHealth());
                    healthRotationX90.setRotateAngleX((float) deg);
                }
            }
        };
    }

    private static boolean isSwingLeftHand(EntityMaid maid) {
        return maid.swingingArm == Hand.OFF_HAND;
    }

    @Deprecated
    public static boolean isPassengerMarisaBroom(EntityMaid maid) {
        return false;
    }

    @Deprecated
    private static boolean isPortableAudioPlay() {
        return false;
    }

    @Deprecated
    private static boolean isHoldTrolley() {
        return false;
    }

    @Deprecated
    private static boolean isHoldVehicle() {
        return false;
    }

    @Deprecated
    private static float[] getLeftHandRotation() {
        return FIXED_HAND_ROTATION;
    }

    @Deprecated
    private static float[] getRightHandRotation() {
        return FIXED_HAND_ROTATION;
    }
}
