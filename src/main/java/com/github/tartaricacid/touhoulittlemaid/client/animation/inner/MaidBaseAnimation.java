package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.EntityBaseAnimation.getBaseFloatDefault;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.MaidArmorAnimation.getArmorDefault;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.MaidArmorAnimation.getArmorReverse;

public final class MaidBaseAnimation {
    private static final float[] FIXED_HAND_ROTATION = new float[]{0, 0, 0};

    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/default.js"), getArmDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/extra.js"), getArmExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/swing.js"), getArmSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/vertical.js"), getArmVertical());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/beg.js"), getHeadBeg());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/blink.js"), getHeadBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/default.js"), getHeadDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/extra.js"), getHeadExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/hurt.js"), getHeadHurt());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/music_shake.js"), getHeadMusicShake());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/reverse_blink.js"), getHeadReverseBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/default.js"), getLegDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/extra.js"), getLegExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/vertical.js"), getLegVertical());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/default.js"), getSitDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_hidden.js"), getSitSkirtHidden());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_rotation.js"), getSitSkirtRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack.js"), getStatusBackpack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack_level.js"), getStatusBackpackLevel());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/sasimono.js"), getStatusSasimono());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/tail/default.js"), getTailDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/wing/default.js"), getWingDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sleep/default.js"), getSleepDefault());

        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid.default.js"), getMaidDefault());
    }

    public static IAnimation<EntityMaid> getHeadDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper head = modelMap.get("head");
                if (head != null) {
                    head.setRotateAngleX(headPitch * 0.017453292f);
                    head.setRotateAngleY(netHeadYaw * 0.017453292f);
                    if (maid.isSleeping()) {
                        head.setRotateAngleX(15 * 0.017453292f);
                    }
                }

                ModelRendererWrapper hat = modelMap.get("hat");
                if (hat != null) {
                    hat.setHidden(maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHeadBlink() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper blink = modelMap.get("blink");

                if (blink != null) {
                    if (maid.isSleeping()) {
                        blink.setHidden(false);
                        return;
                    }
                    float remainder = (ageInTicks + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 60;
                    blink.setHidden(!(55 < remainder && remainder < 60));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getHeadBeg() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper head = modelMap.get("head");
                ModelRendererWrapper ahoge = modelMap.get("ahoge");
                ModelRendererWrapper ahogeShow = modelMap.get("ahogeShow");

                if (maid.isBegging()) {
                    if (head != null) {
                        head.setRotateAngleZ(0.139f);
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleX((float) (Math.cos(ageInTicks * 1.0) * 0.05));
                        ahoge.setRotateAngleZ((float) (Math.sin(ageInTicks * 1.0) * 0.05));
                    }
                    if (ahogeShow != null) {
                        ahogeShow.setHidden(false);
                    }
                } else {
                    if (head != null) {
                        head.setRotateAngleZ(0);
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleZ(0);
                    }
                    if (ahogeShow != null) {
                        ahogeShow.setHidden(true);
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


    public static IAnimation<EntityMaid> getHeadMusicShake() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper head = modelMap.get("head");

                if (head != null) {
                    if (isPortableAudioPlay()) {
                        head.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.4) * 0.06));
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getLegDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                ModelRendererWrapper legRight = modelMap.get("legRight");

                boolean isFarm = "farm".equals(maid.getTask().getUid().getPath()) && maid.swingTime > 0.0;

                if (isFarm) {
                    GlWrapper.translate(0, 0.0713625, -0.35876875);
                    GlWrapper.rotate(22.5f, 1, 0, 0);
                }

                if (legLeft != null) {
                    double leftRad = Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount;
                    if (isFarm) {
                        leftRad -= 0.3927;
                    }
                    legLeft.setRotateAngleX((float) leftRad);
                    legLeft.setRotateAngleY(0);
                    legLeft.setRotateAngleZ(0);
                }
                if (legRight != null) {
                    double rightRad = -Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount;
                    if (isFarm) {
                        rightRad -= 0.3927;
                    }
                    legRight.setRotateAngleX((float) rightRad);
                    legRight.setRotateAngleY(0);
                    legRight.setRotateAngleZ(0);
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

    public static IAnimation<EntityMaid> getArmDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                double f1 = 1.0 - Math.pow(1.0 - maid.swingTime, 4);
                double f2 = Math.sin(f1 * Math.PI);
                double f3 = Math.sin(maid.swingTime * Math.PI) * -0.7 * 0.75;

                float[] rotation;
                if (armLeft != null) {
                    if (maid.isSitInJoyBlock()) {
                        armLeft.setRotateAngleX(-1.3f);
                    } else if (isHoldTrolley()) {
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
                        if (maid.swingTime > 0.0 && isSwingLeftHand(maid)) {
                            armLeft.setRotateAngleX((float) (armLeft.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armLeft.setRotateAngleZ((float) (armLeft.getRotateAngleZ() + Math.sin(maid.swingTime * Math.PI) * -0.4));
                        }
                    }
                }

                if (armRight != null) {
                    if (maid.isSitInJoyBlock()) {
                        armRight.setRotateAngleX(-1.3f);
                    } else if (isHoldVehicle()) {
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

    public static IAnimation<EntityMaid> getArmSwing() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                if (maid.isSwingingArms()) {
                    if (armLeft != null) {
                        armLeft.setRotateAngleX(-1.396f);
                        armLeft.setRotateAngleY(0.785f);
                    }
                    if (armRight != null) {
                        armRight.setRotateAngleX(-1.396f);
                        armRight.setRotateAngleY(-0.174f);
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

    public static IAnimation<EntityMaid> getSitDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper head = modelMap.get("head");
                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                ModelRendererWrapper legRight = modelMap.get("legRight");
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                // 头部复位
                if (head != null) {
                    head.setOffsetY(0);
                }

                if (isPassengerMarisaBroom(maid)) {
                    // 坐在扫帚上时，应用待命的动作
                    ridingBroomPosture(head, armLeft, armRight, legLeft, legRight);
                } else if (maid.isPassenger()) {
                    ridingPosture(legLeft, legRight);
                } else if (maid.isInSittingPose()) {
                    sittingPosture(armLeft, armRight, legLeft, legRight);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getSitSkirtHidden() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sittingHiddenSkirt = modelMap.get("sittingHiddenSkirt");
                if (sittingHiddenSkirt != null) {
                    sittingHiddenSkirt.setHidden(isPassengerMarisaBroom(maid) || maid.isPassenger() || maid.isInSittingPose());
                }

                ModelRendererWrapper reverseSittingHiddenSkirt = modelMap.get("_sittingHiddenSkirt");
                if (reverseSittingHiddenSkirt != null) {
                    reverseSittingHiddenSkirt.setHidden(!isPassengerMarisaBroom(maid) && !maid.isPassenger() && !maid.isInSittingPose());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getSitSkirtRotation() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sittingRotationSkirt = modelMap.get("sittingRotationSkirt");
                if (sittingRotationSkirt != null) {
                    if (isPassengerMarisaBroom(maid) || maid.isPassenger() || maid.isInSittingPose()) {
                        sittingRotationSkirt.setRotateAngleX(-0.567f);
                    } else {
                        sittingRotationSkirt.setRotateAngleX(0);
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getWingDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper wingLeft = modelMap.get("wingLeft");
                ModelRendererWrapper wingRight = modelMap.get("wingRight");

                if (wingLeft != null) {
                    wingLeft.setRotateAngleY((float) (-Math.cos(ageInTicks * 0.3) * 0.2 + 1.0));
                    wingLeft.setHidden(maid.isSleeping());
                }
                if (wingRight != null) {
                    wingRight.setRotateAngleY((float) (Math.cos(ageInTicks * 0.3) * 0.2 - 1.0));
                    wingRight.setHidden(maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getSleepDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sleepHide = modelMap.get("sleepHide");
                ModelRendererWrapper sleepShow = modelMap.get("sleepShow");

                if (sleepHide != null) {
                    sleepHide.setHidden(maid.isSleeping());
                }

                if (sleepShow != null) {
                    sleepShow.setHidden(!maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getStatusBackpack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper backpackShow = modelMap.get("backpackShow");
                ModelRendererWrapper backpackHidden = modelMap.get("backpackHidden");

                if (backpackShow != null) {
                    backpackShow.setHidden(!maid.hasBackpack());
                }
                if (backpackHidden != null) {
                    backpackHidden.setHidden(maid.hasBackpack());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getStatusBackpackLevel() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper backpackLevelEmpty = modelMap.get("backpackLevelEmpty");
                ModelRendererWrapper backpackLevelSmall = modelMap.get("backpackLevelSmall");
                ModelRendererWrapper backpackLevelMiddle = modelMap.get("backpackLevelMiddle");
                ModelRendererWrapper backpackLevelBig = modelMap.get("backpackLevelBig");

                int level = maid.getBackpackLevel();

                if (backpackLevelEmpty != null) {
                    backpackLevelEmpty.setHidden(level != 0);
                }

                if (backpackLevelSmall != null) {
                    backpackLevelSmall.setHidden(level != 1);
                }

                if (backpackLevelMiddle != null) {
                    backpackLevelMiddle.setHidden(level != 2);
                }

                if (backpackLevelBig != null) {
                    backpackLevelBig.setHidden(level != 3);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getStatusSasimono() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sasimonoShow = modelMap.get("sasimonoShow");
                ModelRendererWrapper sasimonoHidden = modelMap.get("sasimonoHidden");

                if (sasimonoShow != null) {
                    sasimonoShow.setHidden(!maid.hasSasimono());
                }
                if (sasimonoHidden != null) {
                    sasimonoHidden.setHidden(maid.hasSasimono());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTailDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper tail = modelMap.get("tail");

                if (tail != null) {
                    tail.setRotateAngleX((float) (Math.sin(ageInTicks * 0.2) * 0.05));
                    tail.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.2) * 0.1));

                    tail.setHidden(maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getMaidDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                getHeadDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getHeadBlink().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getHeadBeg().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getHeadMusicShake().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getLegDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getArmDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getArmSwing().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getArmVertical().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getSitDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getArmorDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getArmorReverse().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getWingDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getTailDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getSitSkirtRotation().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
                getBaseFloatDefault().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, maid, modelMap);
            }
        };
    }

    private static void ridingPosture(ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        if (legLeft != null) {
            legLeft.setRotateAngleX(-1.134f);
            legLeft.setRotateAngleZ(-0.262f);
        }
        if (legRight != null) {
            legRight.setRotateAngleX(-1.134f);
            legRight.setRotateAngleZ(0.262f);
        }
        GlWrapper.translate(0, 0.3, 0);
    }

    private static void sittingPosture(ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        if (armLeft != null) {
            armLeft.setRotateAngleX(-0.798f);
            armLeft.setRotateAngleZ(0.274f);
        }
        if (armRight != null) {
            armRight.setRotateAngleX(-0.798f);
            armRight.setRotateAngleZ(-0.274f);
        }
        ridingPosture(legLeft, legRight);
    }

    private static void ridingBroomPosture(ModelRendererWrapper head, ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        sittingPosture(armLeft, armRight, legLeft, legRight);
        if (head != null) {
            head.setRotateAngleX((float) (head.getRotateAngleX() - 30 * Math.PI / 180));
            head.setOffsetY(0.0625f);
        }
        GlWrapper.rotate(30, 1, 0, 0);
        GlWrapper.translate(0, -0.4, -0.3);
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
