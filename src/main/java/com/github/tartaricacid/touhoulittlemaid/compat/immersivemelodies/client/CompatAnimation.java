package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.client;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer;
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
}
