package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;

public interface IAnimation<T extends LivingEntity> {
    /**
     * 实体动画
     */
    default void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch, float scaleFactor, T entityIn, HashMap<String, ModelRendererWrapper> modelMap) {
    }
}
