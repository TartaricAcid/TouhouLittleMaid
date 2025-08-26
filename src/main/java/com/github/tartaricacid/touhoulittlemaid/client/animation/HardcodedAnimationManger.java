package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.special.SwimAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.animation.special.TridentAnimation;
import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.ImmersiveMelodiesCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;
import java.util.List;

/**
 * 随着后续版本的更新，一些新的动画会加入，并且采用硬编码进行修改
 * <p>
 * 这里就是适用于女仆的新动画调用的地方
 */
public final class HardcodedAnimationManger {
    private static final List<ICustomAnimation<? extends LivingEntity>> ANIMATIONS = Lists.newLinkedList();

    public static void init() {
        HardcodedAnimationManger manager = new HardcodedAnimationManger();

        // 游泳动画
        manager.addMaidAnimation(new SwimAnimation());
        // 三叉戟使用动画
        manager.addMaidAnimation(new TridentAnimation());

        // Immersive Melodies 乐器模组兼容
        ImmersiveMelodiesCompat.addAnimation(manager);

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addHardcodeAnimation(manager);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void playMaidAnimation(IMaid maid, HashMap<String, ModelRendererWrapper> models,
                                         float limbSwing, float limbSwingAmount, float ageInTicks,
                                         float netHeadYaw, float headPitch) {
        for (ICustomAnimation animation : ANIMATIONS) {
            animation.setRotationAngles(maid.asEntity(), models, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void playGeckoMaidAnimation(IMaid maid, AnimatedGeoModel model,
                                              float limbSwing, float limbSwingAmount, float ageInTicks,
                                              float netHeadYaw, float headPitch) {
        for (ICustomAnimation animation : ANIMATIONS) {
            animation.setGeckoRotationAngles(maid.asEntity(), model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setupRotations(Mob entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, boolean isGecko) {
        for (ICustomAnimation animation : ANIMATIONS) {
            if (isGecko) {
                animation.setupGeckoRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
            } else {
                animation.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
            }
        }
    }

    public void addMaidAnimation(ICustomAnimation<Mob> animation) {
        ANIMATIONS.add(animation);
    }
}
