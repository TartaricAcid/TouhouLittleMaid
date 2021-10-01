package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Date;
import java.util.HashMap;

public final class InnerAnimation {
    private static final HashMap<ResourceLocation, IAnimation> INNER_ANIMATION = Maps.newHashMap();

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
                    float remainder = (ageInTicks + maid.getUUID().getLeastSignificantBits() % 10) % 60;
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

                if (maid.isBegging()) {
                    if (head != null) {
                        head.setRotateAngleZ(0.139f);
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleX((float) (Math.cos(ageInTicks * 1.0) * 0.05));
                        ahoge.setRotateAngleZ((float) (Math.sin(ageInTicks * 1.0) * 0.05));
                    }
                } else {
                    if (head != null) {
                        head.setRotateAngleZ(0);
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleZ(0);
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
                ModelRendererWrapper reverseBlink = modelMap.get("_bink");
                if (reverseBlink != null) {
                    float remainder = (ageInTicks + maid.getUUID().getLeastSignificantBits() % 10) % 60;
                    reverseBlink.setHidden(55 < remainder && remainder < 60);
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
                    if (isPortableAudioPlay(maid)) {
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
                    } else if (isHoldTrolley(maid)) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle(maid)) {
                        rotation = getLeftHandRotation(maid);
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
                    } else if (isHoldVehicle(maid)) {
                        rotation = getRightHandRotation(maid);
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
                    if (isHoldTrolley(maid)) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle(maid)) {
                        rotation = getLeftHandRotation(maid);
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
                    if (isHoldVehicle(maid)) {
                        rotation = getRightHandRotation(maid);
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

                boolean valueIsNormal = (0 < maid.getArmorValue()) && (maid.getArmorValue() <= 5);

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

    public static IAnimation<EntityMaid> getArmorWeatherRainging() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper helmetWeatherRainging = modelMap.get("helmetWeatherRainging");
                ModelRendererWrapper chestPlateWeatherRainging = modelMap.get("chestPlateWeatherRainging");
                ModelRendererWrapper chestPlateLeftWeatherRainging = modelMap.get("chestPlateLeftWeatherRainging");
                ModelRendererWrapper chestPlateMiddleWeatherRainging = modelMap.get("chestPlateMiddleWeatherRainging");
                ModelRendererWrapper chestPlateRightWeatherRainging = modelMap.get("chestPlateRightWeatherRainging");
                ModelRendererWrapper leggingsWeatherRainging = modelMap.get("leggingsWeatherRainging");
                ModelRendererWrapper leggingsLeftWeatherRainging = modelMap.get("leggingsLeftWeatherRainging");
                ModelRendererWrapper leggingsMiddleWeatherRainging = modelMap.get("leggingsMiddleWeatherRainging");
                ModelRendererWrapper leggingsRightWeatherRainging = modelMap.get("leggingsRightWeatherRainging");
                ModelRendererWrapper bootsLeftWeatherRainging = modelMap.get("bootsLeftWeatherRainging");
                ModelRendererWrapper bootsRightWeatherRainging = modelMap.get("bootsRightWeatherRainging");

                boolean weatherIsRainging = maid.level.isRaining();

                if (helmetWeatherRainging != null) {
                    helmetWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (chestPlateWeatherRainging != null) {
                    chestPlateWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (chestPlateLeftWeatherRainging != null) {
                    chestPlateLeftWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (chestPlateMiddleWeatherRainging != null) {
                    chestPlateMiddleWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (chestPlateRightWeatherRainging != null) {
                    chestPlateRightWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (leggingsWeatherRainging != null) {
                    leggingsWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (leggingsLeftWeatherRainging != null) {
                    leggingsLeftWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (leggingsMiddleWeatherRainging != null) {
                    leggingsMiddleWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (leggingsRightWeatherRainging != null) {
                    leggingsRightWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (bootsLeftWeatherRainging != null) {
                    bootsLeftWeatherRainging.setHidden(!weatherIsRainging);
                }
                if (bootsRightWeatherRainging != null) {
                    bootsRightWeatherRainging.setHidden(!weatherIsRainging);
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

    public static IAnimation<EntityMaid> getHealthLessShow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper healthLessQuarterShow = modelMap.get("healthLessQuarterShow");
                ModelRendererWrapper healthLessHalfShow = modelMap.get("healthLessHalfShow");
                ModelRendererWrapper healthLessThreeQuartersShow = modelMap.get("healthLessThreeQuartersShow");

                double i = maid.getHealth() / maid.getMaxHealth();

                if (healthLessQuarterShow != null) {
                    healthLessQuarterShow.setHidden(i > 0.25);
                }

                if (healthLessHalfShow != null) {
                    healthLessHalfShow.setHidden(i > 0.5);
                }

                if (healthLessThreeQuartersShow != null) {
                    healthLessThreeQuartersShow.setHidden(i > 0.75);
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

                double i = maid.getHealth() / maid.getMaxHealth();

                if (healthMoreQuarterShow != null) {
                    healthMoreQuarterShow.setHidden(i <= 0.25);
                }

                if (healthMoreHalfShow != null) {
                    healthMoreHalfShow.setHidden(i <= 0.5);
                }

                if (healthMoreThreeQuartersShow != null) {
                    healthMoreThreeQuartersShow.setHidden(i <= 0.75);
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

    public static IAnimation<EntityMaid> getStatusMoving() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper movingShow = modelMap.get("movingShow");
                ModelRendererWrapper movingHidden = modelMap.get("movingHidden");

                if (movingShow != null) {
                    movingShow.setHidden(limbSwing <= 0);
                }
                if (movingHidden != null) {
                    movingHidden.setHidden(limbSwing > 0);
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

    public static IAnimation<EntityMaid> getTaskAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper attackHidden = modelMap.get("attackHidden");

                if (attackHidden != null) {
                    attackHidden.setHidden("attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper attackShow = modelMap.get("attackShow");
                if (attackShow != null) {
                    attackShow.setHidden(!"attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskDanmakuAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper danmakuAttackHidden = modelMap.get("danmakuAttackHidden");

                if (danmakuAttackHidden != null) {
                    danmakuAttackHidden.setHidden("danmaku_attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper danmakuAttackShow = modelMap.get("danmakuAttackShow");
                if (danmakuAttackShow != null) {
                    danmakuAttackShow.setHidden(!"danmaku_attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFarm() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper farmHidden = modelMap.get("farmHidden");

                if (farmHidden != null) {
                    farmHidden.setHidden("farm".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper farmShow = modelMap.get("farmShow");
                if (farmShow != null) {
                    farmShow.setHidden(!"farm".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFeedAnimal() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper feed_animalHidden = modelMap.get("feedAnimalHidden");

                if (feed_animalHidden != null) {
                    feed_animalHidden.setHidden("feed_animal".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper feed_animalShow = modelMap.get("feedAnimalShow");
                if (feed_animalShow != null) {
                    feed_animalShow.setHidden(!"feed_animal".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskIdle() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper idleHidden = modelMap.get("idleHidden");

                if (idleHidden != null) {
                    idleHidden.setHidden("idle".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper idleShow = modelMap.get("idleShow");
                if (idleShow != null) {
                    idleShow.setHidden(!"idle".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskMilk() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper milkHidden = modelMap.get("milkHidden");

                if (milkHidden != null) {
                    milkHidden.setHidden("milk".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper milkShow = modelMap.get("milkShow");
                if (milkShow != null) {
                    milkShow.setHidden(!"milk".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskShears() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper shearsHidden = modelMap.get("shearsHidden");

                if (shearsHidden != null) {
                    shearsHidden.setHidden("shears".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper shearsShow = modelMap.get("shearsShow");
                if (shearsShow != null) {
                    shearsShow.setHidden(!"shears".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskSugarCane() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sugar_caneHidden = modelMap.get("sugarCaneHidden");

                if (sugar_caneHidden != null) {
                    sugar_caneHidden.setHidden("sugar_cane".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper sugar_caneShow = modelMap.get("sugarCaneShow");
                if (sugar_caneShow != null) {
                    sugar_caneShow.setHidden(!"sugar_cane".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskCocoa() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper cocoaHidden = modelMap.get("cocoaHidden");

                if (cocoaHidden != null) {
                    cocoaHidden.setHidden("cocoa".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper cocoaShow = modelMap.get("cocoaShow");
                if (cocoaShow != null) {
                    cocoaShow.setHidden(!"cocoa".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskExtinguishing() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper extinguishingHidden = modelMap.get("extinguishingHidden");

                if (extinguishingHidden != null) {
                    extinguishingHidden.setHidden("extinguishing".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper extinguishingShow = modelMap.get("extinguishingShow");
                if (extinguishingShow != null) {
                    extinguishingShow.setHidden(!"extinguishing".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFeed() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper feedHidden = modelMap.get("feedHidden");

                if (feedHidden != null) {
                    feedHidden.setHidden("feed".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper feedShow = modelMap.get("feedShow");
                if (feedShow != null) {
                    feedShow.setHidden(!"feed".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskGrass() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper grassHidden = modelMap.get("grassHidden");

                if (grassHidden != null) {
                    grassHidden.setHidden("grass".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper grassShow = modelMap.get("grassShow");
                if (grassShow != null) {
                    grassShow.setHidden(!"grass".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskMelon() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper melonHidden = modelMap.get("melonHidden");

                if (melonHidden != null) {
                    melonHidden.setHidden("melon".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper melonShow = modelMap.get("melonShow");
                if (melonShow != null) {
                    melonShow.setHidden(!"melon".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskRangedAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper ranged_attackHidden = modelMap.get("ranged_attackHidden");

                if (ranged_attackHidden != null) {
                    ranged_attackHidden.setHidden("ranged_attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper ranged_attackShow = modelMap.get("ranged_attackShow");
                if (ranged_attackShow != null) {
                    ranged_attackShow.setHidden(!"ranged_attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskSnow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper snowHidden = modelMap.get("snowHidden");

                if (snowHidden != null) {
                    snowHidden.setHidden("snow".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper snowShow = modelMap.get("snowShow");
                if (snowShow != null) {
                    snowShow.setHidden(!"snow".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskTorch() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper torchHidden = modelMap.get("torchHidden");

                if (torchHidden != null) {
                    torchHidden.setHidden("torch".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper torchShow = modelMap.get("torchShow");
                if (torchShow != null) {
                    torchShow.setHidden(!"torch".equals(maid.getTask().getUid().getPath()));
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

    public static IAnimation<EntityMaid> getSpecialHecatia() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper earthHair = modelMap.get("earthHair");
                ModelRendererWrapper logoEarth = modelMap.get("logoEarth");
                ModelRendererWrapper earthTop = modelMap.get("earthTop");
                ModelRendererWrapper earthSideLeft = modelMap.get("earthSideLeft");
                ModelRendererWrapper earthSideRight = modelMap.get("earthSideRight");

                ModelRendererWrapper moonHair = modelMap.get("moonHair");
                ModelRendererWrapper logoMoon = modelMap.get("logoMoon");
                ModelRendererWrapper moonTop = modelMap.get("moonTop");
                ModelRendererWrapper moonSideLeft = modelMap.get("moonSideLeft");
                ModelRendererWrapper moonSideRight = modelMap.get("moonSideRight");

                ModelRendererWrapper otherHair = modelMap.get("otherHair");
                ModelRendererWrapper logoOther = modelMap.get("logoOther");
                ModelRendererWrapper otherTop = modelMap.get("otherTop");
                ModelRendererWrapper otherSideLeft = modelMap.get("otherSideLeft");
                ModelRendererWrapper otherSideRight = modelMap.get("otherSideRight");

                int dim = maid.getDim();
                if (dim == 0) {
                    earthHair.setHidden(false);
                    logoEarth.setHidden(false);
                    earthTop.setHidden(false);
                    earthSideLeft.setHidden(true);
                    earthSideRight.setHidden(true);

                    moonHair.setHidden(true);
                    logoMoon.setHidden(true);
                    moonTop.setHidden(true);
                    moonSideLeft.setHidden(true);
                    moonSideRight.setHidden(false);

                    otherHair.setHidden(true);
                    logoOther.setHidden(true);
                    otherTop.setHidden(true);
                    otherSideLeft.setHidden(false);
                    otherSideRight.setHidden(true);
                } else if (dim == 1) {
                    earthHair.setHidden(true);
                    logoEarth.setHidden(true);
                    earthTop.setHidden(true);
                    earthSideLeft.setHidden(false);
                    earthSideRight.setHidden(true);

                    moonHair.setHidden(false);
                    logoMoon.setHidden(false);
                    moonTop.setHidden(false);
                    moonSideLeft.setHidden(true);
                    moonSideRight.setHidden(true);

                    otherHair.setHidden(true);
                    logoOther.setHidden(true);
                    otherTop.setHidden(true);
                    otherSideLeft.setHidden(true);
                    otherSideRight.setHidden(false);
                } else {
                    earthHair.setHidden(true);
                    logoEarth.setHidden(true);
                    earthTop.setHidden(true);
                    earthSideLeft.setHidden(false);
                    earthSideRight.setHidden(true);

                    moonHair.setHidden(true);
                    logoMoon.setHidden(true);
                    moonTop.setHidden(true);
                    moonSideLeft.setHidden(true);
                    moonSideRight.setHidden(false);

                    otherHair.setHidden(false);
                    logoOther.setHidden(false);
                    otherTop.setHidden(false);
                    otherSideLeft.setHidden(true);
                    otherSideRight.setHidden(true);
                }

                if (maid.hasHelmet()) {
                    earthTop.setHidden(true);
                    moonTop.setHidden(true);
                    otherTop.setHidden(true);
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getSpecialWakasagihime() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                if (maid.isInSittingPose()) {
                    if (armLeft != null) {
                        armLeft.setRotateAngleX(-0.798f);
                        armLeft.setRotateAngleZ(0.274f);
                    }
                    if (armRight != null) {
                        armRight.setRotateAngleX(-0.798f);
                        armRight.setRotateAngleZ(-0.274f);
                    }
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getPlayerArmDefault() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                double f1 = 1.0 - Math.pow(1.0 - maid.attackAnim, 4);
                double f2 = Math.sin(f1 * Math.PI);
                double f3 = Math.sin(maid.attackAnim * Math.PI) * -0.7 * 0.75;

                if (armLeft != null) {
                    if (maid.isSitInJoyBlock()) {
                        armLeft.setRotateAngleX(-1.8f);
                    } else if (isHoldTrolley(maid)) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle(maid)) {
                        float[] rotation = getLeftHandRotation(maid);
                        armLeft.setRotateAngleX(rotation[0]);
                        armLeft.setRotateAngleY(rotation[1]);
                        armLeft.setRotateAngleZ(rotation[2]);
                    } else {
                        armLeft.setRotateAngleX((float) (-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount));
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.05) * 0.025 - 0.05));
                        // 手部使用动画
                        if (maid.attackAnim > 0.0 && isSwingLeftHand(maid)) {
                            armLeft.setRotateAngleX((float) (armLeft.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armLeft.setRotateAngleZ(armLeft.getRotateAngleZ() + (float) (Math.sin(maid.attackAnim * Math.PI) * -0.4));
                        }
                    }
                }

                if (armRight != null) {
                    if (maid.isSitInJoyBlock()) {
                        armRight.setRotateAngleX(-1.8f);
                    } else if (isHoldVehicle(maid)) {
                        float[] rotation = getRightHandRotation(maid);
                        armRight.setRotateAngleX(rotation[0]);
                        armRight.setRotateAngleY(rotation[1]);
                        armRight.setRotateAngleZ(rotation[2]);
                    } else {
                        armRight.setRotateAngleX((float) (Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount));
                        armRight.setRotateAngleY(0);
                        armRight.setRotateAngleZ((float) (-Math.cos(ageInTicks * 0.05) * 0.025 + 0.05));
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

    public static IAnimation<EntityMaid> getPlayerSitDefault() {
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
                    playerRidingBroomPosture(head, armLeft, armRight, legLeft, legRight);
                } else if (maid.isPassenger()) {
                    playerRidingPosture(legLeft, legRight);
                } else if (maid.isInSittingPose()) {
                    playerSittingPosture(armLeft, armRight, legLeft, legRight);
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

    public static IAnimation<LivingEntity> getBaseDimDefault() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                RegistryKey<World> dim = entity.level.dimension();

                ModelRendererWrapper overWorldHidden = modelMap.get("overWorldHidden");
                ModelRendererWrapper overWorldShow = modelMap.get("overWorldShow");
                ModelRendererWrapper netherWorldHidden = modelMap.get("netherWorldHidden");
                ModelRendererWrapper netherWorldShow = modelMap.get("netherWorldShow");
                ModelRendererWrapper endWorldHidden = modelMap.get("endWorldHidden");
                ModelRendererWrapper endWorldShow = modelMap.get("endWorldShow");

                if (overWorldHidden != null) {
                    overWorldHidden.setHidden(dim.equals(World.OVERWORLD));
                }
                if (overWorldShow != null) {
                    overWorldShow.setHidden(!dim.equals(World.OVERWORLD));
                }
                if (netherWorldHidden != null) {
                    netherWorldHidden.setHidden(dim.equals(World.NETHER));
                }
                if (netherWorldShow != null) {
                    netherWorldShow.setHidden(!dim.equals(World.OVERWORLD));
                }
                if (endWorldHidden != null) {
                    endWorldHidden.setHidden(dim.equals(World.END));
                }
                if (endWorldShow != null) {
                    endWorldShow.setHidden(!dim.equals(World.END));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseFloatDefault() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sinFloat = modelMap.get("sinFloat");
                ModelRendererWrapper cosFloat = modelMap.get("cosFloat");
                ModelRendererWrapper negativeSinFloat = modelMap.get("_sinFloat");
                ModelRendererWrapper negativeCosFloat = modelMap.get("_cosFloat");

                if (sinFloat != null) {
                    sinFloat.setOffsetY((float) (Math.sin(ageInTicks * 0.1) * 0.05));
                }
                if (cosFloat != null) {
                    cosFloat.setOffsetY((float) (Math.cos(ageInTicks * 0.1) * 0.05));
                }
                if (negativeSinFloat != null) {
                    negativeSinFloat.setOffsetY((float) (-Math.sin(ageInTicks * 0.1) * 0.05));
                }
                if (negativeCosFloat != null) {
                    negativeCosFloat.setOffsetY((float) (-Math.cos(ageInTicks * 0.1) * 0.05));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseTimeDayNight() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                World world = entity.level;
                ModelRendererWrapper dayShow = modelMap.get("dayShow");
                ModelRendererWrapper nightShow = modelMap.get("nightShow");

                if (dayShow != null) {
                    dayShow.setHidden(world.getDayTime() >= 13000);
                }

                if (nightShow != null) {
                    nightShow.setHidden(world.getDayTime() < 13000);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseTimeGameRotation() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                long time = entity.level.getDayTime();
                float hourDeg = (float) (Math.PI + ((time / 1000) % 12) * (Math.PI / 6));
                float minDeg = (float) (((time % 1000) / (50 / 3)) * (Math.PI / 30));

                ModelRendererWrapper gameHourRotationX = modelMap.get("gameHourRotationX");
                ModelRendererWrapper gameMinuteRotationX = modelMap.get("gameMinuteRotationX");
                ModelRendererWrapper gameHourRotationY = modelMap.get("gameHourRotationY");
                ModelRendererWrapper gameMinuteRotationY = modelMap.get("gameMinuteRotationY");
                ModelRendererWrapper gameHourRotationZ = modelMap.get("gameHourRotationZ");
                ModelRendererWrapper gameMinuteRotationZ = modelMap.get("gameMinuteRotationZ");

                if (gameHourRotationX != null) {
                    gameHourRotationX.setRotateAngleX(hourDeg);
                }

                if (gameMinuteRotationX != null) {
                    gameMinuteRotationX.setRotateAngleX(minDeg);
                }

                if (gameHourRotationY != null) {
                    gameHourRotationY.setRotateAngleY(hourDeg);
                }

                if (gameMinuteRotationY != null) {
                    gameMinuteRotationY.setRotateAngleY(minDeg);
                }

                if (gameHourRotationZ != null) {
                    gameHourRotationZ.setRotateAngleZ(hourDeg);
                }

                if (gameMinuteRotationZ != null) {
                    gameMinuteRotationZ.setRotateAngleZ(minDeg);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseTimeSysRotation() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                Date date = new Date();
                float hourDeg = (float) (((date.getHours() + date.getMinutes() / 60) % 12) * (Math.PI / 6));
                float minDeg = (float) ((date.getMinutes() + date.getSeconds() / 60) * (Math.PI / 30));
                float secDeg = (float) (date.getSeconds() * (Math.PI / 30));

                ModelRendererWrapper systemHourRotationX = modelMap.get("systemHourRotationX");
                ModelRendererWrapper systemMinuteRotationX = modelMap.get("systemMinuteRotationX");
                ModelRendererWrapper systemSecondRotationX = modelMap.get("systemSecondRotationX");
                ModelRendererWrapper systemHourRotationY = modelMap.get("systemHourRotationY");
                ModelRendererWrapper systemMinuteRotationY = modelMap.get("systemMinuteRotationY");
                ModelRendererWrapper systemSecondRotationY = modelMap.get("systemSecondRotationY");
                ModelRendererWrapper systemHourRotationZ = modelMap.get("systemHourRotationZ");
                ModelRendererWrapper systemMinuteRotationZ = modelMap.get("systemMinuteRotationZ");
                ModelRendererWrapper systemSecondRotationZ = modelMap.get("systemSecondRotationZ");

                if (systemHourRotationX != null) {
                    systemHourRotationX.setRotateAngleX(hourDeg);
                }

                if (systemMinuteRotationX != null) {
                    systemMinuteRotationX.setRotateAngleX(minDeg);
                }

                if (systemSecondRotationX != null) {
                    systemSecondRotationX.setRotateAngleX(secDeg);
                }

                if (systemHourRotationY != null) {
                    systemHourRotationY.setRotateAngleY(hourDeg);
                }

                if (systemMinuteRotationY != null) {
                    systemMinuteRotationY.setRotateAngleY(minDeg);
                }

                if (systemSecondRotationY != null) {
                    systemSecondRotationY.setRotateAngleY(secDeg);
                }

                if (systemHourRotationZ != null) {
                    systemHourRotationZ.setRotateAngleZ(hourDeg);
                }

                if (systemMinuteRotationZ != null) {
                    systemMinuteRotationZ.setRotateAngleZ(minDeg);
                }

                if (systemSecondRotationZ != null) {
                    systemSecondRotationZ.setRotateAngleZ(secDeg);
                }
            }
        };
    }

    public static IAnimation<EntityChair> getPassengerHidden() {
        return new IAnimation<EntityChair>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityChair chair, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper passengerHidden = modelMap.get("passengerHidden");
                ModelRendererWrapper passengerShow = modelMap.get("passengerShow");

                if (passengerHidden != null) {
                    passengerHidden.setHidden(chair.hasPassenger());
                }
                if (passengerShow != null) {
                    passengerShow.setHidden(!chair.hasPassenger());
                }
            }
        };
    }

    public static IAnimation<EntityChair> getPassengerRotation() {
        return new IAnimation<EntityChair>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityChair chair, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper passengerRotationYaw = modelMap.get("passengerRotationYaw");
                ModelRendererWrapper passengerRotationPitch = modelMap.get("passengerRotationPitch");

                if (passengerRotationYaw != null) {
                    passengerRotationYaw.setRotateAngleY((chair.getPassengerYaw() - chair.getYaw()) * 0.017453292f);
                }
                if (passengerRotationPitch != null) {
                    passengerRotationPitch.setRotateAngleX(chair.getPassengerPitch() * 0.017453292f);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationReciprocate() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper xReciprocate = modelMap.get("xReciprocate");
                ModelRendererWrapper yReciprocate = modelMap.get("yReciprocate");
                ModelRendererWrapper zReciprocate = modelMap.get("zReciprocate");

                if (xReciprocate != null) {
                    xReciprocate.setRotateAngleX((float) (Math.cos(ageInTicks * 0.3) * 0.2));
                }
                if (yReciprocate != null) {
                    yReciprocate.setRotateAngleY((float) (Math.cos(ageInTicks * 0.3) * 0.2));
                }
                if (zReciprocate != null) {
                    zReciprocate.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.3) * 0.2));
                }
            }
        };
    }


    public static IAnimation<LivingEntity> getBaseRotationXH() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper xRotationHighA = modelMap.get("xRotationHighA");
                if (xRotationHighA != null) {
                    xRotationHighA.setRotateAngleX((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationHighB = modelMap.get("xRotationHighB");
                if (xRotationHighB != null) {
                    xRotationHighB.setRotateAngleX((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationHighC = modelMap.get("xRotationHighC");
                if (xRotationHighC != null) {
                    xRotationHighC.setRotateAngleX((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationHighD = modelMap.get("xRotationHighD");
                if (xRotationHighD != null) {
                    xRotationHighD.setRotateAngleX((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationHighE = modelMap.get("xRotationHighE");
                if (xRotationHighE != null) {
                    xRotationHighE.setRotateAngleX((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationXN() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netNeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper xRotationNormalA = modelMap.get("xRotationNormalA");
                if (xRotationNormalA != null) {
                    xRotationNormalA.setRotateAngleX((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationNormalB = modelMap.get("xRotationNormalB");
                if (xRotationNormalB != null) {
                    xRotationNormalB.setRotateAngleX((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationNormalC = modelMap.get("xRotationNormalC");
                if (xRotationNormalC != null) {
                    xRotationNormalC.setRotateAngleX((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationNormalD = modelMap.get("xRotationNormalD");
                if (xRotationNormalD != null) {
                    xRotationNormalD.setRotateAngleX((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationNormalE = modelMap.get("xRotationNormalE");
                if (xRotationNormalE != null) {
                    xRotationNormalE.setRotateAngleX((float) (ageInTicks % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationXL() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper xRotationLowA = modelMap.get("xRotationLowA");
                if (xRotationLowA != null) {
                    xRotationLowA.setRotateAngleX((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationLowB = modelMap.get("xRotationLowB");
                if (xRotationLowB != null) {
                    xRotationLowB.setRotateAngleX((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationLowC = modelMap.get("xRotationLowC");
                if (xRotationLowC != null) {
                    xRotationLowC.setRotateAngleX((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationLowD = modelMap.get("xRotationLowD");
                if (xRotationLowD != null) {
                    xRotationLowD.setRotateAngleX((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper xRotationLowE = modelMap.get("xRotationLowE");
                if (xRotationLowE != null) {
                    xRotationLowE.setRotateAngleX((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationYH() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper yRotationHighA = modelMap.get("yRotationHighA");
                if (yRotationHighA != null) {
                    yRotationHighA.setRotateAngleY((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationHighB = modelMap.get("yRotationHighB");
                if (yRotationHighB != null) {
                    yRotationHighB.setRotateAngleY((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationHighC = modelMap.get("yRotationHighC");
                if (yRotationHighC != null) {
                    yRotationHighC.setRotateAngleY((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationHighD = modelMap.get("yRotationHighD");
                if (yRotationHighD != null) {
                    yRotationHighD.setRotateAngleY((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationHighE = modelMap.get("yRotationHighE");
                if (yRotationHighE != null) {
                    yRotationHighE.setRotateAngleY((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationYN() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netNeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper yRotationNormalA = modelMap.get("yRotationNormalA");
                if (yRotationNormalA != null) {
                    yRotationNormalA.setRotateAngleY((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationNormalB = modelMap.get("yRotationNormalB");
                if (yRotationNormalB != null) {
                    yRotationNormalB.setRotateAngleY((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationNormalC = modelMap.get("yRotationNormalC");
                if (yRotationNormalC != null) {
                    yRotationNormalC.setRotateAngleY((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationNormalD = modelMap.get("yRotationNormalD");
                if (yRotationNormalD != null) {
                    yRotationNormalD.setRotateAngleY((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationNormalE = modelMap.get("yRotationNormalE");
                if (yRotationNormalE != null) {
                    yRotationNormalE.setRotateAngleY((float) (ageInTicks % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationYL() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper yRotationLowA = modelMap.get("yRotationLowA");
                if (yRotationLowA != null) {
                    yRotationLowA.setRotateAngleY((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationLowB = modelMap.get("yRotationLowB");
                if (yRotationLowB != null) {
                    yRotationLowB.setRotateAngleY((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationLowC = modelMap.get("yRotationLowC");
                if (yRotationLowC != null) {
                    yRotationLowC.setRotateAngleY((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationLowD = modelMap.get("yRotationLowD");
                if (yRotationLowD != null) {
                    yRotationLowD.setRotateAngleY((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper yRotationLowE = modelMap.get("yRotationLowE");
                if (yRotationLowE != null) {
                    yRotationLowE.setRotateAngleY((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationZH() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper zRotationHighA = modelMap.get("zRotationHighA");
                if (zRotationHighA != null) {
                    zRotationHighA.setRotateAngleZ((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationHighB = modelMap.get("zRotationHighB");
                if (zRotationHighB != null) {
                    zRotationHighB.setRotateAngleZ((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationHighC = modelMap.get("zRotationHighC");
                if (zRotationHighC != null) {
                    zRotationHighC.setRotateAngleZ((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationHighD = modelMap.get("zRotationHighD");
                if (zRotationHighD != null) {
                    zRotationHighD.setRotateAngleZ((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationHighE = modelMap.get("zRotationHighE");
                if (zRotationHighE != null) {
                    zRotationHighE.setRotateAngleZ((float) ((ageInTicks * 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationZN() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netNeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper zRotationNormalA = modelMap.get("zRotationNormalA");
                if (zRotationNormalA != null) {
                    zRotationNormalA.setRotateAngleZ((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationNormalB = modelMap.get("zRotationNormalB");
                if (zRotationNormalB != null) {
                    zRotationNormalB.setRotateAngleZ((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationNormalC = modelMap.get("zRotationNormalC");
                if (zRotationNormalC != null) {
                    zRotationNormalC.setRotateAngleZ((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationNormalD = modelMap.get("zRotationNormalD");
                if (zRotationNormalD != null) {
                    zRotationNormalD.setRotateAngleZ((float) (ageInTicks % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationNormalE = modelMap.get("zRotationNormalE");
                if (zRotationNormalE != null) {
                    zRotationNormalE.setRotateAngleZ((float) (ageInTicks % 360 * 0.017453292));
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getBaseRotationZL() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper zRotationLowA = modelMap.get("zRotationLowA");
                if (zRotationLowA != null) {
                    zRotationLowA.setRotateAngleZ((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationLowB = modelMap.get("zRotationLowB");
                if (zRotationLowB != null) {
                    zRotationLowB.setRotateAngleZ((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationLowC = modelMap.get("zRotationLowC");
                if (zRotationLowC != null) {
                    zRotationLowC.setRotateAngleZ((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationLowD = modelMap.get("zRotationLowD");
                if (zRotationLowD != null) {
                    zRotationLowD.setRotateAngleZ((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }

                ModelRendererWrapper zRotationLowE = modelMap.get("zRotationLowE");
                if (zRotationLowE != null) {
                    zRotationLowE.setRotateAngleZ((float) ((ageInTicks / 4) % 360 * 0.017453292));
                }
            }
        };
    }

    public static void playerRidingPosture(ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        if (legLeft != null) {
            legLeft.setRotateAngleX(-1.4f);
            legLeft.setRotateAngleY(-0.4f);
        }

        if (legRight != null) {
            legRight.setRotateAngleX(-1.4f);
            legRight.setRotateAngleY(0.4f);
        }

        GlWrapper.translate(0, 0.3, 0);
    }

    public static void playerSittingPosture(ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
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

    public static void playerRidingBroomPosture(ModelRendererWrapper head, ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        sittingPosture(armLeft, armRight, legLeft, legRight);
        if (head != null) {
            head.setRotateAngleX((float) (head.getRotateAngleX() - 30 * Math.PI / 180));
            head.setOffsetY(0.0625f);
        }

        GlWrapper.rotate(30, 1, 0, 0);
        GlWrapper.translate(0, -0.45, -0.3);
    }

    @Deprecated
    public static boolean isPassengerMarisaBroom(EntityMaid maid) {
        return false;
    }

    public static void ridingPosture(ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
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

    public static void sittingPosture(ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
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

    public static void ridingBroomPosture(ModelRendererWrapper head, ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        sittingPosture(armLeft, armRight, legLeft, legRight);
        if (head != null) {
            head.setRotateAngleX((float) (head.getRotateAngleX() - 30 * Math.PI / 180));
            head.setOffsetY(0.0625f);
        }
        GlWrapper.rotate(30, 1, 0, 0);
        GlWrapper.translate(0, -0.4, -0.3);
    }

    @Deprecated
    public static boolean isPortableAudioPlay(EntityMaid maid) {
        return false;
    }

    @Deprecated
    public static boolean isHoldTrolley(EntityMaid maid) {
        return false;
    }

    @Deprecated
    public static boolean isHoldVehicle(EntityMaid maid) {
        return false;
    }

    public static boolean isSwingLeftHand(EntityMaid maid) {
        return maid.swingingArm == Hand.OFF_HAND;
    }

    @Deprecated
    public static float[] getLeftHandRotation(EntityMaid maid) {
        return new float[]{0, 0, 0};
    }

    @Deprecated
    public static float[] getRightHandRotation(EntityMaid maid) {
        return new float[]{0, 0, 0};
    }

    public static HashMap<ResourceLocation, IAnimation> getInnerAnimation() {
        return INNER_ANIMATION;
    }

    public static void init() {
        INNER_ANIMATION.clear();
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/default.js"), getArmDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/extra.js"), getArmExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/swing.js"), getArmSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/vertical.js"), getArmVertical());
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
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/weather/raining.js"), getArmorWeatherRainging());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/armor/weather/thundering.js"), getArmorWeatherThundering());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/beg.js"), getHeadBeg());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/blink.js"), getHeadBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/default.js"), getHeadDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/extra.js"), getHeadExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/hurt.js"), getHeadHurt());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/music_shake.js"), getHeadMusicShake());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/reverse_blink.js"), getHeadReverseBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/less_show.js"), getHealthLessShow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/more_show.js"), getHealthMoreShow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/health/rotation.js"), getHealthRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/default.js"), getLegDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/extra.js"), getLegExtra());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/vertical.js"), getLegVertical());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/default.js"), getSitDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_hidden.js"), getSitSkirtHidden());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_rotation.js"), getSitSkirtRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack.js"), getStatusBackpack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack_level.js"), getStatusBackpackLevel());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/moving.js"), getStatusMoving());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/sasimono.js"), getStatusSasimono());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/tail/default.js"), getTailDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/attack.js"), getTaskAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/danmaku_attack.js"), getTaskDanmakuAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/farm.js"), getTaskFarm());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/feed_animal.js"), getTaskFeedAnimal());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/idle.js"), getTaskIdle());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/milk.js"), getTaskMilk());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/shears.js"), getTaskShears());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/sugar_cane.js"), getTaskSugarCane());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/cocoa.js"), getTaskCocoa());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/extinguishing.js"), getTaskExtinguishing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/feed.js"), getTaskFeed());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/grass.js"), getTaskGrass());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/melon.js"), getTaskMelon());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/ranged_attack.js"), getTaskRangedAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/snow.js"), getTaskSnow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/torch.js"), getTaskTorch());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/wing/default.js"), getWingDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/wing/default.js"), getWingDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sleep/default.js"), getSleepDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/player/arm/default.js"), getPlayerArmDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/player/sit/default.js"), getPlayerSitDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid.default.js"), getMaidDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/special/hecatia_dimension.js"), getSpecialHecatia());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/special/wakasagihime_sit.js"), getSpecialWakasagihime());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/chair/passenger/hidden.js"), getPassengerHidden());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/chair/passenger/rotation.js"), getPassengerRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/dimension/default.js"), getBaseDimDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/float/default.js"), getBaseFloatDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/time/day_night_hidden.js"), getBaseTimeDayNight());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/time/game_rotation.js"), getBaseTimeGameRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/time/system_rotation.js"), getBaseTimeSysRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/reciprocate.js"), getBaseRotationReciprocate());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/x_high_speed.js"), getBaseRotationXH());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/x_normal_speed.js"), getBaseRotationXN());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/x_low_speed.js"), getBaseRotationXL());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/y_high_speed.js"), getBaseRotationYH());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/y_normal_speed.js"), getBaseRotationYN());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/y_low_speed.js"), getBaseRotationYL());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/z_high_speed.js"), getBaseRotationZH());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/z_normal_speed.js"), getBaseRotationZN());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/rotation/z_low_speed.js"), getBaseRotationZL());
    }
}
