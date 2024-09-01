package com.github.tartaricacid.touhoulittlemaid.compat.tacz.client;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class GunMaidRender {
    public static void addItemTranslate(PoseStack matrixStack, ItemStack itemStack, boolean isLeft) {
        if (!isLeft && itemStack.getItem() instanceof IGun) {
            matrixStack.translate(0, -0.125, 0);
        }
    }

    public static void renderBackGun(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, ItemStack stack, IMaid maid) {
        if (!(stack.getItem() instanceof IGun)) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        matrixStack.translate(0, 0.5, -0.25);
        if (InGameMaidConfig.INSTANCE.isShowBackpack()) {
            maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
        } else {
            BackpackManager.getEmptyBackpack().offsetBackpackItem(matrixStack);
        }
        {
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35));
            matrixStack.scale(0.6f, 0.6f, 0.6f);
            Mob mob = maid.asEntity();
            Minecraft.getInstance().getItemRenderer().renderStatic(mob, stack, ItemTransforms.TransformType.FIXED, false, matrixStack, bufferIn, mob.level, packedLightIn, OverlayTexture.NO_OVERLAY, mob.getId());
            matrixStack.popPose();
        }
        matrixStack.popPose();
    }

    public static void renderBackGun(ItemStack heldItem, AnimatedGeoModel geoModel, IMaid maid, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        IGun gun = IGun.getIGunOrNull(heldItem);
        if (gun == null) {
            return;
        }
        Mob entity = maid.asEntity();
        IMaidBackpack maidBackpackType = maid.getMaidBackpackType();
        // 如果女仆穿戴了背包，且配置文件允许显示背包
        // 直接调用背包渲染
        if (InGameMaidConfig.INSTANCE.isShowBackpack() && maidBackpackType != BackpackManager.getEmptyBackpack()) {
            if (!geoModel.backpackBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, geoModel.backpackBones());
            }
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            poseStack.translate(0, -1, 0.25);
            renderBackGun(poseStack, buffer, packedLight, heldItem, maid);
            return;
        }
        TimelessAPI.getCommonGunIndex(gun.getGunId(heldItem)).ifPresent(index -> {
            String weaponType = index.getType();
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            if (isPistol(weaponType) && !geoModel.tacPistolBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, geoModel.tacPistolBones());

                poseStack.translate(0, -0.125, 0);
                poseStack.scale(0.65f, 0.65f, 0.65f);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                renderer.renderStatic(heldItem, ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getId());
            }
            if (!isPistol(weaponType) && !geoModel.tacRifleBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, geoModel.tacRifleBones());

                poseStack.scale(0.65f, 0.65f, 0.65f);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-180.0F));
                renderer.renderStatic(heldItem, ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getId());
            }
        });
    }

    private static boolean isPistol(String type) {
        return type.equals(GunTabType.PISTOL.name().toLowerCase(Locale.ENGLISH));
    }
}
