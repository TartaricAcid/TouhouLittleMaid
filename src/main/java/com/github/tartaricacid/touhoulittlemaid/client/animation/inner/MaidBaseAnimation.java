package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.TacCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.EntityBaseAnimation.getBaseFloatDefault;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.MaidArmorAnimation.getArmorDefault;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.MaidArmorAnimation.getArmorReverse;
import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.MaidExtraAnimation.getArmVertical;

public final class MaidBaseAnimation {
    private static final float[] FIXED_HAND_ROTATION = new float[]{0, 0, 0};

    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/default.js"), getArmDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/arm/swing.js"), getArmSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/beg.js"), getHeadBeg());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/blink.js"), getHeadBlink());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/default.js"), getHeadDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/music_shake.js"), getHeadMusicShake());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/ear_shake.js"), getEarShake());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/ear_beg_shake.js"), getEarBegShake());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/hair_swing.js"), getHairSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/head/hair_ponytail_swing.js"), getHairPonytailSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/leg/default.js"), getLegDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/default.js"), getSitDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/no_leg.js"), getSitNoLeg());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_hidden.js"), getSitSkirtHidden());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_rotation.js"), getSitSkirtRotation());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sit/skirt_rotation_swing.js"), getSitSkirtRotationSwing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack.js"), getStatusBackpack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/backpack_level.js"), getStatusBackpackLevel());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/status/sasimono.js"), getStatusSasimono());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/tail/default.js"), getTailDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/wing/default.js"), getWingDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/sleep/default.js"), getSleepDefault());

        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid.default.js"), getMaidDefault());
    }

    public static IAnimation<Mob> getHeadDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
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

    public static IAnimation<Mob> getHeadBlink() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper blink = modelMap.get("blink");
                ModelRendererWrapper blink2 = modelMap.get("blink2");

                if (blink != null) {
                    if (maid.isSleeping()) {
                        blink.setHidden(false);
                        return;
                    }
                    float remainder = (ageInTicks + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 60;
                    blink.setHidden(!(55 < remainder && remainder < 60));
                }

                if (blink2 != null) {
                    if (maid.isSleeping()) {
                        blink2.setHidden(false);
                        return;
                    }
                    float remainder = (ageInTicks + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 60;
                    blink2.setHidden(!(55 < remainder && remainder < 60));
                }
            }
        };
    }

    public static IAnimation<Mob> getHeadBeg() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;
                ModelRendererWrapper head = modelMap.get("head");
                ModelRendererWrapper ahoge = modelMap.get("ahoge");
                ModelRendererWrapper begShow = modelMap.get("begShow");

                if (maid.isBegging()) {
                    if (head != null) {
                        head.setRotateAngleZ(0.139f);
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleX((float) (Math.cos(ageInTicks * 1.0) * 0.05) + ahoge.getInitRotateAngleX());
                        ahoge.setRotateAngleZ((float) (Math.sin(ageInTicks * 1.0) * 0.05) + ahoge.getInitRotateAngleZ());
                    }
                    if (begShow != null) {
                        begShow.setHidden(false);
                    }
                } else {
                    if (head != null) {
                        head.setRotateAngleZ(head.getInitRotateAngleZ());
                    }
                    if (ahoge != null) {
                        ahoge.setRotateAngleZ(ahoge.getInitRotateAngleZ());
                    }
                    if (begShow != null) {
                        begShow.setHidden(true);
                    }
                }
            }
        };
    }


    public static IAnimation<Mob> getHeadMusicShake() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper head = modelMap.get("head");

                if (head != null) {
                    if (isPortableAudioPlay()) {
                        head.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.4) * 0.06) + head.getInitRotateAngleZ());
                    }
                }
            }
        };
    }

    public static IAnimation<Mob> getHairPonytailSwing() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper hairPonytailSwing = modelMap.get("hairPonytailSwing");

                if (hairPonytailSwing != null) {
                    float rotationZ = (float) Math.sin(ageInTicks * 0.05) * 0.06f;
                    hairPonytailSwing.setRotateAngleZ(rotationZ + hairPonytailSwing.getInitRotateAngleZ());
                }
            }
        };
    }

    public static IAnimation<Mob> getHairSwing() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper hairLeftSwing = modelMap.get("hairLeftSwing");
                ModelRendererWrapper hairRightSwing = modelMap.get("hairRightSwing");

                float rotationZ = (float) Math.sin(ageInTicks * 0.05) * 0.04f;
                if (hairLeftSwing != null) {
                    hairLeftSwing.setRotateAngleZ(hairLeftSwing.getInitRotateAngleZ() + rotationZ);
                }
                if (hairRightSwing != null) {
                    hairRightSwing.setRotateAngleZ(hairRightSwing.getInitRotateAngleZ() - rotationZ);
                }
            }
        };
    }

    public static IAnimation<Mob> getEarShake() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper earLeftShake = modelMap.get("earLeftShake");
                ModelRendererWrapper earRightShake = modelMap.get("earRightShake");

                float time = (ageInTicks + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 40;
                if (time < Math.PI * 4) {
                    float rotationZ = (float) Math.abs(Math.sin(time * 0.25)) * 0.4f;
                    if (earLeftShake != null) {
                        earLeftShake.setRotateAngleZ(earLeftShake.getInitRotateAngleZ() + rotationZ);
                    }
                    if (earRightShake != null) {
                        earRightShake.setRotateAngleZ(earRightShake.getInitRotateAngleZ() - rotationZ);
                    }
                } else {
                    if (earLeftShake != null) {
                        earLeftShake.setRotateAngleZ(earLeftShake.getInitRotateAngleZ());
                    }
                    if (earRightShake != null) {
                        earRightShake.setRotateAngleZ(earRightShake.getInitRotateAngleZ());
                    }
                }
            }
        };
    }

    public static IAnimation<Mob> getEarBegShake() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper earLeftShake = modelMap.get("earLeftBegShake");
                ModelRendererWrapper earRightShake = modelMap.get("earRightBegShake");

                float time = (ageInTicks + Math.abs(mob.getUUID().getLeastSignificantBits()) % 10) % 40;
                if (maid.isBegging() && time < Math.PI * 4) {
                    float rotationZ = (float) Math.abs(Math.sin(time * 0.25)) * 0.4f;
                    if (earLeftShake != null) {
                        earLeftShake.setRotateAngleZ(earLeftShake.getInitRotateAngleZ() + rotationZ);
                    }
                    if (earRightShake != null) {
                        earRightShake.setRotateAngleZ(earRightShake.getInitRotateAngleZ() - rotationZ);
                    }
                } else {
                    if (earLeftShake != null) {
                        earLeftShake.setRotateAngleZ(earLeftShake.getInitRotateAngleZ());
                    }
                    if (earRightShake != null) {
                        earRightShake.setRotateAngleZ(earRightShake.getInitRotateAngleZ());
                    }
                }
            }
        };
    }

    public static IAnimation<Mob> getLegDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                ModelRendererWrapper legRight = modelMap.get("legRight");

                boolean isFarm = "farm".equals(maid.getTask().getUid().getPath()) && maid.asEntity().swingTime > 0;

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
                    legLeft.setRotateAngleY(legLeft.getInitRotateAngleY());
                    legLeft.setRotateAngleZ(legLeft.getInitRotateAngleZ());
                }
                if (legRight != null) {
                    double rightRad = -Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount;
                    if (isFarm) {
                        rightRad -= 0.3927;
                    }
                    legRight.setRotateAngleX((float) rightRad);
                    legRight.setRotateAngleY(legRight.getInitRotateAngleY());
                    legRight.setRotateAngleZ(legRight.getInitRotateAngleZ());
                }
            }
        };
    }

    public static IAnimation<Mob> getArmDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                double f1 = 1.0 - Math.pow(1.0 - mob.attackAnim, 4);
                double f2 = Math.sin(f1 * Math.PI);
                double f3 = Math.sin(mob.attackAnim * Math.PI) * -0.7 * 0.75;

                float[] rotation;
                if (armLeft != null) {
                    if (maid.isSitInJoyBlock()) {
                        armLeft.setRotateAngleX(-1.3f);
                    } else if (isHoldTrolley()) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(armLeft.getInitRotateAngleY());
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle()) {
                        rotation = getLeftHandRotation();
                        armLeft.setRotateAngleX(rotation[0]);
                        armLeft.setRotateAngleY(rotation[1]);
                        armLeft.setRotateAngleZ(rotation[2]);
                    } else {
                        armLeft.setRotateAngleX((float) (-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount));
                        armLeft.setRotateAngleY(armLeft.getInitRotateAngleY());
                        armLeft.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.05) * 0.05 + armLeft.getInitRotateAngleZ()));
                        // 手部攻击动画
                        if (mob.attackAnim > 0.0 && isSwingLeftHand(mob)) {
                            armLeft.setRotateAngleX((float) (armLeft.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armLeft.setRotateAngleZ((float) (armLeft.getRotateAngleZ() + Math.sin(mob.attackAnim * Math.PI) * -0.4));
                        }
                        // 使用动画
                        if (mob.isUsingItem() && mob.getUsedItemHand() == InteractionHand.OFF_HAND) {
                            armLeft.setRotateAngleX((float) (armLeft.getInitRotateAngleX() - Math.PI * 80 / 180.0));
                            armLeft.setRotateAngleY((float) (armLeft.getInitRotateAngleY() + Math.PI * 25 / 180.0));
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
                        armRight.setRotateAngleY(armRight.getInitRotateAngleY());
                        armRight.setRotateAngleZ((float) (-Math.cos(ageInTicks * 0.05) * 0.05 + armRight.getInitRotateAngleZ()));
                        // 手部攻击动画
                        if (mob.attackAnim > 0.0 && !isSwingLeftHand(mob)) {
                            armRight.setRotateAngleX((float) (armRight.getRotateAngleX() - (f2 * 1.2 + f3)));
                            armRight.setRotateAngleZ((float) (armRight.getRotateAngleZ() + Math.sin(mob.attackAnim * Math.PI) * -0.4));
                        }
                        // 使用动画
                        if (mob.isUsingItem() && mob.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                            armRight.setRotateAngleX((float) (armRight.getInitRotateAngleX() - Math.PI * 80 / 180.0));
                            armRight.setRotateAngleY((float) (armRight.getInitRotateAngleY() - Math.PI * 20 / 180.0));
                        }
                    }
                }
            }
        };
    }


    public static IAnimation<Mob> getArmSwing() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                if (maid.isSwingingArms() && !TacCompat.onSwingGun(maid, armLeft, armRight)) {
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


    public static IAnimation<Mob> getSitDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper head = modelMap.get("head");
                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                ModelRendererWrapper legRight = modelMap.get("legRight");
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                // 头部复位
                if (head != null) {
                    head.setOffsetY(0);
                }

                if (isPassengerMarisaBroom(mob)) {
                    // 坐在扫帚上时，应用待命的动作
                    ridingBroomPosture(head, armLeft, armRight, legLeft, legRight);
                } else if (mob.isPassenger()) {
                    ridingPosture(legLeft, legRight);
                } else if (maid.isMaidInSittingPose()) {
                    sittingPosture(armLeft, armRight, legLeft, legRight);
                }
            }
        };
    }

    public static IAnimation<Mob> getSitNoLeg() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper head = modelMap.get("head");
                ModelRendererWrapper legLeft = modelMap.get("legLeft");
                ModelRendererWrapper legRight = modelMap.get("legRight");
                ModelRendererWrapper armLeft = modelMap.get("armLeft");
                ModelRendererWrapper armRight = modelMap.get("armRight");

                // 头部复位
                if (head != null) {
                    head.setOffsetY(0);
                }

                if (isPassengerMarisaBroom(mob)) {
                    // 坐在扫帚上时，应用待命的动作
                    ridingBroomPosture(head, armLeft, armRight, legLeft, legRight);
                } else if (mob.isPassenger()) {
                    ridingPosture(legLeft, legRight);
                } else if (maid.isMaidInSittingPose()) {
                    sittingNoLegPosture(armLeft, armRight);
                }
            }
        };
    }

    public static IAnimation<Mob> getSitSkirtHidden() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper sittingHiddenSkirt = modelMap.get("sittingHiddenSkirt");
                if (sittingHiddenSkirt != null) {
                    sittingHiddenSkirt.setHidden(isPassengerMarisaBroom(mob) || mob.isPassenger() || maid.isMaidInSittingPose());
                }

                ModelRendererWrapper reverseSittingHiddenSkirt = modelMap.get("_sittingHiddenSkirt");
                if (reverseSittingHiddenSkirt != null) {
                    reverseSittingHiddenSkirt.setHidden(!isPassengerMarisaBroom(mob) && !mob.isPassenger() && !maid.isMaidInSittingPose());
                }
            }
        };
    }

    public static IAnimation<Mob> getSitSkirtRotation() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper sittingRotationSkirt = modelMap.get("sittingRotationSkirt");
                if (sittingRotationSkirt != null) {
                    if (isPassengerMarisaBroom(mob) || mob.isPassenger() || maid.isMaidInSittingPose()) {
                        sittingRotationSkirt.setRotateAngleX(-0.567f);
                    } else {
                        sittingRotationSkirt.setRotateAngleX(sittingRotationSkirt.getInitRotateAngleX());
                    }
                }
            }
        };
    }

    public static IAnimation<Mob> getSitSkirtRotationSwing() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

                ModelRendererWrapper sittingRotationSwingSkirt = modelMap.get("sittingRotationSwingSkirt");
                if (sittingRotationSwingSkirt != null) {
                    if (isPassengerMarisaBroom(mob) || mob.isPassenger() || maid.isMaidInSittingPose()) {
                        sittingRotationSwingSkirt.setRotateAngleX(-0.567f);
                        sittingRotationSwingSkirt.setRotateAngleZ(sittingRotationSwingSkirt.getInitRotateAngleZ());
                    } else {
                        sittingRotationSwingSkirt.setRotateAngleX(sittingRotationSwingSkirt.getInitRotateAngleX());
                        float rotationZ = (float) Math.sin(ageInTicks * 0.05) * 0.03f;
                        sittingRotationSwingSkirt.setRotateAngleZ(sittingRotationSwingSkirt.getInitRotateAngleZ() + rotationZ);
                    }
                }
            }
        };
    }

    public static IAnimation<Mob> getWingDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper wingLeft = modelMap.get("wingLeft");
                ModelRendererWrapper wingRight = modelMap.get("wingRight");

                if (wingLeft != null) {
                    wingLeft.setRotateAngleY((float) (-Math.cos(ageInTicks * 0.3) * 0.2 + wingLeft.getInitRotateAngleY()));
                    wingLeft.setHidden(maid.isSleeping());
                }
                if (wingRight != null) {
                    wingRight.setRotateAngleY((float) (Math.cos(ageInTicks * 0.3) * 0.2 + wingRight.getInitRotateAngleY()));
                    wingRight.setHidden(maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<Mob> getSleepDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
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

    public static IAnimation<Mob> getStatusBackpack() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

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

    @Deprecated
    public static IAnimation<Mob> getStatusBackpackLevel() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
            }
        };
    }

    public static IAnimation<Mob> getStatusSasimono() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob mob, HashMap<String, ModelRendererWrapper> modelMap) {
                IMaid maid = IMaid.convert(mob);
                if (maid == null) return;

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

    public static IAnimation<Mob> getTailDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper tail = modelMap.get("tail");

                if (tail != null) {
                    tail.setRotateAngleX((float) (Math.sin(ageInTicks * 0.2) * 0.05) + tail.getInitRotateAngleX());
                    tail.setRotateAngleZ((float) (Math.cos(ageInTicks * 0.2) * 0.1) + tail.getInitRotateAngleZ());

                    tail.setHidden(maid.isSleeping());
                }
            }
        };
    }

    public static IAnimation<Mob> getMaidDefault() {
        return new IAnimation<Mob>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Mob maid, HashMap<String, ModelRendererWrapper> modelMap) {
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

    private static void sittingNoLegPosture(ModelRendererWrapper armLeft, ModelRendererWrapper armRight) {
        if (armLeft != null) {
            armLeft.setRotateAngleX(-0.798f);
            armLeft.setRotateAngleZ(0.274f);
        }
        if (armRight != null) {
            armRight.setRotateAngleX(-0.798f);
            armRight.setRotateAngleZ(-0.274f);
        }
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

    private static boolean isSwingLeftHand(Mob maid) {
        return maid.swingingArm == InteractionHand.OFF_HAND;
    }

    @Deprecated
    public static boolean isPassengerMarisaBroom(Mob maid) {
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
