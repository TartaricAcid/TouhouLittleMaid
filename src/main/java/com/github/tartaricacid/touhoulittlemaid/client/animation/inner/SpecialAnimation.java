package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class SpecialAnimation {
    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/special/hecatia_dimension.js"), getSpecialHecatia());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/special/wakasagihime_sit.js"), getSpecialWakasagihime());
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
}
