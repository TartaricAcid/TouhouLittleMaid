package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import immersive_melodies.client.animation.EntityModelAnimator;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation.getPartOrNull;

public class CompatAnimation implements ICustomAnimation<Mob> {
    @Override
    public void setRotationAngles(Mob maid, HashMap<String, ? extends IModelRenderer> models,
                                  float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float headPitch) {
        BedrockPart head = getPartOrNull(models, "head");
        BedrockPart hat = getPartOrNull(models, "hat");
        BedrockPart armLeft = getPartOrNull(models, "armLeft");
        BedrockPart armRight = getPartOrNull(models, "armRight");
        EntityModelAnimator.setAngles(new MaidArmsAndHeadAccessor(maid, head, hat, armLeft, armRight));
    }

    @Override
    public void setGeckoRotationAngles(Mob maid, AnimatedGeoModel model,
                                       float limbSwing, float limbSwingAmount,
                                       float ageInTicks, float netHeadYaw, float headPitch) {
        AnimatedGeoBone head = model.head();
        AnimatedGeoBone hat = model.hat();
        AnimatedGeoBone leftArm = model.leftArm();
        AnimatedGeoBone rightArm = model.rightArm();
        EntityModelAnimator.setAngles(new GeckoMaidArmsAndHeadAccessor(maid, head, hat, leftArm, rightArm));
    }
}
