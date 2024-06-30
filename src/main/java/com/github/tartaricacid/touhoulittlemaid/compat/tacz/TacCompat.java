package com.github.tartaricacid.touhoulittlemaid.compat.tacz;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunBaseAnimation;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunGeckoAnimation;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunMaidRender;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.task.TaskGunAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class TacCompat {
    private static final String TACZ_ID = "tacz";
    private static boolean INSTALLED = false;

    public static void addGunTask(TaskManager manager) {
        if (ModList.get().isLoaded(TACZ_ID)) {
            manager.add(new TaskGunAttack());
            INSTALLED = true;
        }
    }

    @Nullable
    public static ResourceLocation getGunId(ItemStack stack) {
        if (INSTALLED) {
            return TacInnerCompat.getGunId(stack);
        }
        return null;
    }

    public static boolean isGun(ItemStack stack) {
        if (INSTALLED) {
            return TacInnerCompat.isGun(stack);
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean onSwingGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
        if (INSTALLED) {
            return GunBaseAnimation.onSwingGun(maid, armLeft, armRight);
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void addItemTranslate(PoseStack matrixStack, ItemStack itemStack, boolean isLeft) {
        if (INSTALLED) {
            GunMaidRender.addItemTranslate(matrixStack, itemStack, isLeft);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderBackGun(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, ItemStack stack, IMaid maid) {
        if (INSTALLED) {
            GunMaidRender.renderBackGun(matrixStack, bufferIn, packedLightIn, stack, maid);
        }
    }

    public static boolean isGrenade(ItemStack itemStack) {
        // TODO 手雷还没有
        return false;
    }

    public static void renderBackGun(ItemStack offhandItem, GeoModel geoModel, IMaid maid, PoseStack poseStack, int packedLight) {
        if (INSTALLED && isGun(offhandItem)) {
            poseStack.pushPose();
            GunMaidRender.renderBackGun(offhandItem, geoModel, maid.asEntity(), poseStack, packedLight);
            poseStack.popPose();
        }
    }

    @Nullable
    public static PlayState playGunMainAnimation(IMaid maid, AnimationEvent<GeckoMaidEntity> event, String animationName, ILoopType loopType) {
        if (INSTALLED && isGun(maid.asEntity().getMainHandItem())) {
            return GunGeckoAnimation.playGunMainAnimation(event, animationName, loopType);
        }
        return null;
    }

    @Nullable
    public static PlayState playGunHoldAnimation(ItemStack mainHandItem, AnimationEvent<GeckoMaidEntity> event) {
        if (INSTALLED && isGun(mainHandItem)) {
            return GunGeckoAnimation.playGunHoldAnimation(event, mainHandItem);
        }
        return null;
    }
}
