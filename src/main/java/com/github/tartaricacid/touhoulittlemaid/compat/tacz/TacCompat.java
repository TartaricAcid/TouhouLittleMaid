//package com.github.tartaricacid.touhoulittlemaid.compat.tacz;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
//import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
//import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
//import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunBaseAnimation;
//import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunGeckoAnimation;
//import com.github.tartaricacid.touhoulittlemaid.compat.tacz.client.GunMaidRender;
//import com.github.tartaricacid.touhoulittlemaid.compat.tacz.event.GunHurtMaidEvent;
//import com.github.tartaricacid.touhoulittlemaid.compat.tacz.task.TaskGunAttack;
//import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.neoforged.neoforge.common.NeoForge;
//import net.neoforged.fml.ModList;
//
//import javax.annotation.Nullable;
//
//public class TacCompat {
//    private static final String TACZ_ID = "tacz";
//    private static boolean INSTALLED = false;
//
//    public static void initAndAddGunTask(TaskManager manager) {
//        if (ModList.get().isLoaded(TACZ_ID)) {
//            NeoForge.EVENT_BUS.register(new GunHurtMaidEvent());
//            manager.add(new TaskGunAttack());
//            INSTALLED = true;
//        }
//    }
//
//    public static boolean isGun(ItemStack stack) {
//        if (INSTALLED) {
//            return TacInnerCompat.isGun(stack);
//        }
//        return false;
//    }
//
//    public static boolean isGrenade(ItemStack itemStack) {
//        // TODO 手雷还没有
//        return false;
//    }
//
//    @Nullable
//    public static ResourceLocation getGunId(ItemStack stack) {
//        if (INSTALLED) {
//            return TacInnerCompat.getGunId(stack);
//        }
//        return null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static boolean onSwingGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
//        if (INSTALLED) {
//            return GunBaseAnimation.onSwingGun(maid, armLeft, armRight);
//        }
//        return false;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void addItemTranslate(PoseStack matrixStack, ItemStack itemStack, boolean isLeft) {
//        if (INSTALLED) {
//            GunMaidRender.addItemTranslate(matrixStack, itemStack, isLeft);
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void renderBackGun(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, ItemStack stack, IMaid maid) {
//        if (INSTALLED) {
//            GunMaidRender.renderBackGun(matrixStack, bufferIn, packedLightIn, stack, maid);
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void renderBackGun(ItemStack offhandItem, GeoModel geoModel, IMaid maid, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
//        if (INSTALLED && isGun(offhandItem)) {
//            poseStack.pushPose();
//            GunMaidRender.renderBackGun(offhandItem, geoModel, maid, poseStack, bufferIn, packedLight);
//            poseStack.popPose();
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Nullable
//    public static PlayState playGunMainAnimation(IMaid maid, AnimationEvent<GeckoMaidEntity> event, String animationName, ILoopType loopType) {
//        if (INSTALLED && isGun(maid.asEntity().getMainHandItem())) {
//            return GunGeckoAnimation.playGunMainAnimation(event, animationName, loopType);
//        }
//        return null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Nullable
//    public static PlayState playGunHoldAnimation(ItemStack mainHandItem, AnimationEvent<GeckoMaidEntity> event) {
//        if (INSTALLED && isGun(mainHandItem)) {
//            return GunGeckoAnimation.playGunHoldAnimation(event, mainHandItem);
//        }
//        return null;
//    }
//}
