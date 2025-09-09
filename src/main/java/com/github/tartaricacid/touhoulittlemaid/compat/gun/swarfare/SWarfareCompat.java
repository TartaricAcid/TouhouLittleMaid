package com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.ai.GunShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.client.GunBaseAnimation;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.client.GunGeckoAnimation;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.client.GunMaidRender;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.event.GunHurtMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.event.MaidSaddleClickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SWarfareCompat {
    public static final ResourceLocation MINIGUN_ID = new ResourceLocation("superbwarfare", "minigun");
    public static final ResourceLocation M_2_HB_ID = new ResourceLocation("superbwarfare", "m_2_hb");

    public static final TagKey<Item> PISTOL = TagKey.create(Registries.ITEM, new ResourceLocation("superbwarfare:animated/pistol"));
    public static final TagKey<Item> SHOOTGUN = TagKey.create(Registries.ITEM, new ResourceLocation("superbwarfare:animated/shootgun"));
    public static final TagKey<Item> SMG = TagKey.create(Registries.ITEM, new ResourceLocation("superbwarfare:animated/smg"));
    public static final TagKey<Item> SNIPER = TagKey.create(Registries.ITEM, new ResourceLocation("superbwarfare:animated/sniper"));
    public static final TagKey<Item> RPG = TagKey.create(Registries.ITEM, new ResourceLocation("superbwarfare:animated/rpg"));

    private static final String MOD_ID = "superbwarfare";
    private static boolean INSTALLED = false;

    public static boolean init() {
        ModFileInfo modFileById = LoadingModList.get().getModFileById(MOD_ID);
        if (modFileById != null) {
            DefaultArtifactVersion modVersion = new DefaultArtifactVersion(modFileById.versionString());
            INSTALLED = modVersion.compareTo(new DefaultArtifactVersion("0.8.7.1")) >= 0;
            if (INSTALLED) {
                MinecraftForge.EVENT_BUS.register(new GunHurtMaidEvent());
                MinecraftForge.EVENT_BUS.register(new MaidSaddleClickEvent());
            }
        }
        return INSTALLED;
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }

    public static boolean isGun(ItemStack stack) {
        if (INSTALLED) {
            return SWarfareCompatInner.isGun(stack);
        }
        return false;
    }

    @Nullable
    public static ResourceLocation getGunId(ItemStack stack) {
        if (INSTALLED) {
            return ForgeRegistries.ITEMS.getKey(stack.getItem());
        }
        return null;
    }

    public static boolean shouldHideLivingRender(LivingEntity entity) {
        if (INSTALLED) {
            return SWarfareCompatInner.shouldHideLivingRender(entity);
        }
        return false;
    }

    public static boolean isGrenade(ItemStack itemStack) {
        if (INSTALLED) {
            return SWarfareCompatInner.isGrenade(itemStack);
        }
        return false;
    }

    public static boolean isVehicle(Entity entity) {
        if (INSTALLED) {
            return SWarfareCompatInner.isVehicle(entity);
        }
        return false;
    }

    public static boolean canSee(EntityMaid maid, LivingEntity target) {
        if (INSTALLED) {
            return SWarfareCompatInner.canSee(maid, target);
        }
        return false;
    }

    public static Optional<Boolean> canVehicleSee(EntityMaid maid, LivingEntity target) {
        if (INSTALLED) {
            return SWarfareCompatInner.canVehicleSee(maid, target);
        }
        return Optional.empty();
    }

    public static int performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem) throws Exception {
        if (INSTALLED) {
            return SWarfareCompatInner.performGunAttack(shooter, target, gunItem);
        }
        return 100;
    }

    public static void tick(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        if (INSTALLED) {
            SWarfareCompatInner.tick(shooter, target, gunItem);
        }
    }

    public static void onStop(EntityMaid maid, GunShootTargetTask task) {
        if (INSTALLED) {
            SWarfareCompatInner.onStop(maid, task);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean onHoldGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
        if (INSTALLED) {
            return GunBaseAnimation.onHoldGun(maid, armLeft, armRight);
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

    @OnlyIn(Dist.CLIENT)
    public static void renderBackGun(ItemStack offhandItem, ILocationModel geoModel, IMaid maid, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
        if (INSTALLED) {
            poseStack.pushPose();
            GunMaidRender.renderBackGun(offhandItem, geoModel, maid, poseStack, bufferIn, packedLight);
            poseStack.popPose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static PlayState playGunMainAnimation(IMaid maid, AnimationEvent<GeckoMaidEntity<?>> event, String animationName, ILoopType loopType) {
        if (INSTALLED && isGun(maid.asEntity().getMainHandItem())) {
            return GunGeckoAnimation.playGunMainAnimation(event, animationName, loopType);
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static PlayState playGunHoldAnimation(ItemStack mainHandItem, AnimationEvent<GeckoMaidEntity<?>> event) {
        if (INSTALLED && isGun(mainHandItem)) {
            return GunGeckoAnimation.playGunHoldAnimation(event, mainHandItem);
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static PlayState playGrenadeAnimation(AnimationEvent<GeckoMaidEntity<?>> event, ItemStack stack, InteractionHand hand) {
        if (INSTALLED && isGrenade(stack)) {
            return GunGeckoAnimation.playGrenadeAnimation(event, hand);
        }
        return null;
    }
}
