package com.github.tartaricacid.touhoulittlemaid.client.animation.special;

import com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.TridentItem;

import java.util.HashMap;

public class TridentAnimation implements ICustomAnimation<Mob> {
    @Override
    public void setRotationAngles(Mob mob, HashMap<String, ? extends IModelRenderer> models,
                                  float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch) {
        if (!mob.isSleeping() && mob.isUsingItem() && mob.getUsedItemHand() == InteractionHand.MAIN_HAND
            && mob.getMainHandItem().getItem() instanceof TridentItem) {
            int tick = mob.getTicksUsingItem();
            BedrockPart armRight = ICustomAnimation.getPartOrNull(models, "armRight");
            if (armRight != null) {
                float partialTick = Minecraft.getInstance().getPartialTick();
                float rot = (tick + partialTick) / 10f;
                armRight.xRot = (armRight.getInitRotX() - 80) - Math.min(rot, Mth.PI / 2) - 10;
                armRight.zRot = -Math.min(rot, Mth.PI / 6);
            }
        }
    }
}
