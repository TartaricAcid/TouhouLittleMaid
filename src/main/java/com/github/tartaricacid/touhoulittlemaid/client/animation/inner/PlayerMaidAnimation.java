package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class PlayerMaidAnimation {
    private static final float[] FIXED_HAND_ROTATION = new float[]{0, 0, 0};

    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/player/arm/default.js"), getPlayerArmDefault());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/player/sit/default.js"), getPlayerSitDefault());
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
                    } else if (isHoldTrolley()) {
                        armLeft.setRotateAngleX(0.5f);
                        armLeft.setRotateAngleY(0);
                        armLeft.setRotateAngleZ(-0.395f);
                    } else if (isHoldVehicle()) {
                        float[] rotation = getLeftHandRotation();
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
                    } else if (isHoldVehicle()) {
                        float[] rotation = getRightHandRotation();
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

                if (isPassengerMarisaBroom()) {
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

    private static void playerRidingPosture(ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
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

    private static void playerSittingPosture(ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
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

    private static void playerRidingBroomPosture(ModelRendererWrapper head, ModelRendererWrapper armLeft, ModelRendererWrapper armRight, ModelRendererWrapper legLeft, ModelRendererWrapper legRight) {
        sittingPosture(armLeft, armRight, legLeft, legRight);
        if (head != null) {
            head.setRotateAngleX((float) (head.getRotateAngleX() - 30 * Math.PI / 180));
            head.setOffsetY(0.0625f);
        }

        GlWrapper.rotate(30, 1, 0, 0);
        GlWrapper.translate(0, -0.45, -0.3);
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

    private static boolean isSwingLeftHand(EntityMaid maid) {
        return maid.swingingArm == Hand.OFF_HAND;
    }

    @Deprecated
    private static boolean isPassengerMarisaBroom() {
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
