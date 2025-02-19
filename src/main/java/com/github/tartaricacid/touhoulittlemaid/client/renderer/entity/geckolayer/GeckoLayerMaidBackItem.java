package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;

import java.util.function.Function;

public class GeckoLayerMaidBackItem<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerMaidRender<T, R> {
    public GeckoLayerMaidBackItem(R entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }
        var model = getGeoMobModel(entity);
        if (model == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!getGeoMob(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible() || RenderFixer.isCarryOnRender(stack, buffer)) {
            return;
        }
        if (entity instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }
        if (stack.getItem() instanceof Vanishable) {
            poseStack.pushPose();

            if (!model.backpackBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, model.backpackBones());
            }
            poseStack.translate(0, 1, 0.25);

            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0, 0.5, -0.25);
            if (entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                maid.getMaidBackpackType().offsetBackpackItem(poseStack);
            } else {
                BackpackManager.getEmptyBackpack().offsetBackpackItem(poseStack);
            }
            if (SlashBladeCompat.isSlashBladeItem(stack)) {
                SlashBladeRender.renderGeckoMaidBackSlashBlade(poseStack, buffer, packedLight, stack);
            } else {
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack, ItemDisplayContext.FIXED, false, poseStack, buffer, entity.level(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
            }
            poseStack.popPose();
            return;
        }

        // TACZ 背部枪械渲染
        TacCompat.renderBackGun(stack, model, maid, poseStack, buffer, packedLight);
    }

    @Override
    public void ysmRender(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }
        var model = getYsmGeoMobModel(entity);
        if (model == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!getYsmGeoMob(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible() || RenderFixer.isCarryOnRender(stack, bufferIn)) {
            return;
        }
        if (entity instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }
        if (stack.getItem() instanceof Vanishable) {
            poseStack.pushPose();

            if (!model.backpackBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, model.backpackBones());
            }
            poseStack.translate(0, 1, 0.25);

            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0, 0.5, -0.25);
            if (entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                maid.getMaidBackpackType().offsetBackpackItem(poseStack);
            } else {
                BackpackManager.getEmptyBackpack().offsetBackpackItem(poseStack);
            }
            if (SlashBladeCompat.isSlashBladeItem(stack)) {
                SlashBladeRender.renderGeckoMaidBackSlashBlade(poseStack, bufferIn, packedLightIn, stack);
            } else {
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack, ItemDisplayContext.FIXED, false, poseStack, bufferIn, entity.level(), packedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
            }
            poseStack.popPose();
            return;
        }

        // TACZ 背部枪械渲染
        TacCompat.renderBackGun(stack, model, maid, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public GeoLayerMaidRender<T, R> create(R geckoEntityMaidRenderer, EntityRendererProvider.Context renderManager, Function<Mob, IGeoEntity> ysmGeoMob) {
        initYsmGeoMobGet(ysmGeoMob);
        return new GeckoLayerMaidBackItem<>(geckoEntityMaidRenderer);
    }
}
