package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class ChairBaseAnimation {
    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/chair/passenger/hidden.js"), getPassengerHidden());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/chair/passenger/rotation.js"), getPassengerRotation());
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
}
