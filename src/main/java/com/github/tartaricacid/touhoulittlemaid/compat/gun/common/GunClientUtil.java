package com.github.tartaricacid.touhoulittlemaid.compat.gun.common;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GunClientUtil {
    public static boolean onHoldGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
        return TacCompat.onHoldGun(maid, armLeft, armRight)
               || SWarfareCompat.onHoldGun(maid, armLeft, armRight);
    }

    public static void addItemTranslate(PoseStack poseStack, ItemStack itemStack, boolean isLeft) {
        if (TacCompat.isGun(itemStack)) {
            TacCompat.addItemTranslate(poseStack, itemStack, isLeft);
        } else if (SWarfareCompat.isGun(itemStack)) {
            SWarfareCompat.addItemTranslate(poseStack, itemStack, isLeft);
        }
    }

    public static void renderBackGun(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, ItemStack stack, IMaid maid) {
        if (TacCompat.isGun(stack)) {
            TacCompat.renderBackGun(matrixStack, bufferIn, packedLightIn, stack, maid);
        } else if (SWarfareCompat.isGun(stack)) {
            SWarfareCompat.renderBackGun(matrixStack, bufferIn, packedLightIn, stack, maid);
        }
    }

    public static void renderBackGun(ItemStack offhandItem, ILocationModel geoModel, IMaid maid, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
        if (TacCompat.isGun(offhandItem)) {
            TacCompat.renderBackGun(offhandItem, geoModel, maid, poseStack, bufferIn, packedLight);
        } else if (SWarfareCompat.isGun(offhandItem)) {
            SWarfareCompat.renderBackGun(offhandItem, geoModel, maid, poseStack, bufferIn, packedLight);
        }
    }

    @Nullable
    public static PlayState playGunMainAnimation(IMaid maid, AnimationEvent<GeckoMaidEntity<?>> event, String animationName, ILoopType loopType) {
        if (TacCompat.isGun(maid.asEntity().getMainHandItem())) {
            return TacCompat.playGunMainAnimation(maid, event, animationName, loopType);
        } else if (SWarfareCompat.isGun(maid.asEntity().getMainHandItem())) {
            return SWarfareCompat.playGunMainAnimation(maid, event, animationName, loopType);
        }
        return null;
    }

    @Nullable
    public static PlayState playGunHoldAnimation(ItemStack mainHandItem, AnimationEvent<GeckoMaidEntity<?>> event) {
        if (TacCompat.isGun(mainHandItem)) {
            return TacCompat.playGunHoldAnimation(mainHandItem, event);
        } else if (SWarfareCompat.isGun(mainHandItem)) {
            return SWarfareCompat.playGunHoldAnimation(mainHandItem, event);
        }
        return null;
    }
}
