package com.github.tartaricacid.touhoulittlemaid.compat.tacz.client;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
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
        matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(-35));
        matrixStack.translate(0.75, -1.25, -0.25);
        matrixStack.scale(0.6f, 0.6f, 0.6f);
        if (InGameMaidConfig.INSTANCE.isShowBackpack()) {
            maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
        } else {
            BackpackManager.getEmptyBackpack().offsetBackpackItem(matrixStack);
        }
        Mob mob = maid.asEntity();
        Minecraft.getInstance().getItemRenderer().renderStatic(mob, stack, ItemDisplayContext.FIXED, false,
                matrixStack, bufferIn, mob.level(), packedLightIn, OverlayTexture.NO_OVERLAY, mob.getId());
        matrixStack.popPose();
    }

    public static void renderBackGun(ItemStack heldItem, GeoModel geoModel, LivingEntity player, PoseStack poseStack, int packedLight) {
        IGun gun = IGun.getIGunOrNull(heldItem);
        if (gun == null) {
            return;
        }
        TimelessAPI.getCommonGunIndex(gun.getGunId(heldItem)).ifPresent(index -> {
            String weaponType = index.getType();
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            if (isPistol(weaponType) && !geoModel.tacPistolBones.isEmpty()) {
                int size = geoModel.tacPistolBones.size();
                for (int i = 0; i < size - 1; i++) {
                    RenderUtils.prepMatrixForBone(poseStack, geoModel.tacPistolBones.get(i));
                }
                GeoBone lastBone = geoModel.tacPistolBones.get(size - 1);
                RenderUtils.translateMatrixToBone(poseStack, lastBone);
                RenderUtils.translateToPivotPoint(poseStack, lastBone);
                RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
                RenderUtils.scaleMatrixForBone(poseStack, lastBone);

                poseStack.translate(0, -0.125, 0);
                poseStack.scale(0.65f, 0.65f, 0.65f);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                renderer.renderStatic(heldItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.level(), player.getId());
            }
            if (!isPistol(weaponType) && !geoModel.tacRifleBones.isEmpty()) {
                int size = geoModel.tacRifleBones.size();
                for (int i = 0; i < size - 1; i++) {
                    RenderUtils.prepMatrixForBone(poseStack, geoModel.tacRifleBones.get(i));
                }
                GeoBone lastBone = geoModel.tacRifleBones.get(size - 1);
                RenderUtils.translateMatrixToBone(poseStack, lastBone);
                RenderUtils.translateToPivotPoint(poseStack, lastBone);
                RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
                RenderUtils.scaleMatrixForBone(poseStack, lastBone);

                poseStack.scale(0.65f, 0.65f, 0.65f);
                poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
                MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                renderer.renderStatic(heldItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.level(), player.getId());
            }
        });
    }

    private static boolean isPistol(String type) {
        return type.equals(GunTabType.PISTOL.name().toLowerCase(Locale.ENGLISH));
    }
}
