package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.v2;

import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity2;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer2;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class GeckoLayerMaidHeld2<T extends Mob, R extends IGeoEntityRenderer2<T>> extends GeoLayerMaidRender2<T, R> {
    private final ItemInHandRenderer itemInHandRenderer;

    public GeckoLayerMaidHeld2(R entityRendererIn, ItemInHandRenderer itemInHandRenderer) {
        super(entityRendererIn);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack offhandItem = entity.getOffhandItem();
        ItemStack mainHandItem = entity.getMainHandItem();
        IAnimatedModel<?> geoModel = getGeoMobModel(entity);
        if (geoModel == null) {
            return;
        }
        if (!offhandItem.isEmpty() || !mainHandItem.isEmpty()) {
            poseStack.pushPose();
            if (!geoModel.rightHandBones().isEmpty() && !RenderFixer.isCarryOnRender(mainHandItem, buffer)) {
                if (SlashBladeCompat.isSlashBladeItem(mainHandItem)) {
                    SlashBladeRender.renderMaidMainhandSlashBlade(entity, geoModel, poseStack, buffer, packedLight, mainHandItem, partialTicks);
                } else {
                    this.renderArmWithItem(entity, mainHandItem, geoModel, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);
                }
            }
            if (!geoModel.leftHandBones().isEmpty() && !RenderFixer.isCarryOnRender(offhandItem, buffer)) {
                if (SlashBladeCompat.isSlashBladeItem(offhandItem)) {
                    SlashBladeRender.renderMaidOffhandSlashBlade(geoModel, poseStack, buffer, packedLight, offhandItem);
                } else {
                    this.renderArmWithItem(entity, offhandItem, geoModel, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, buffer, packedLight);
                }
            }
            poseStack.popPose();
        }
    }

    protected void renderArmWithItem(T livingEntity, ItemStack itemStack, IAnimatedModel<?> geoModel, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!itemStack.isEmpty() && geoModel != null) {
            poseStack.pushPose();
            translateToHand(arm, poseStack, geoModel);
            poseStack.translate(0, -0.0625, -0.1);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            boolean isLeftHand = arm == HumanoidArm.LEFT;
            this.itemInHandRenderer.renderItem(livingEntity, itemStack, displayContext, isLeftHand, poseStack, bufferSource, light);
            poseStack.popPose();
        }
    }

    protected void translateToHand(HumanoidArm arm, PoseStack poseStack, IAnimatedModel<?> geoModel) {
        if (arm == HumanoidArm.LEFT) {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.leftHandBones());
        } else {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.rightHandBones());
        }
    }

    @Override
    public void ysmRender(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack offhandItem = entity.getOffhandItem();
        ItemStack mainHandItem = entity.getMainHandItem();
        IAnimatedModel<?> geoModel = getYsmGeoMobModel(entity);
        if (geoModel == null) {
            return;
        }
        if (!offhandItem.isEmpty() || !mainHandItem.isEmpty()) {
            poseStack.pushPose();
            if (!geoModel.rightHandBones().isEmpty() && !RenderFixer.isCarryOnRender(mainHandItem, bufferIn)) {
                if (SlashBladeCompat.isSlashBladeItem(mainHandItem)) {
                    SlashBladeRender.renderMaidMainhandSlashBlade(entity, geoModel, poseStack, bufferIn, packedLightIn, mainHandItem, partialTicks);
                } else {
                    this.renderArmWithItem(entity, mainHandItem, geoModel, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, bufferIn, packedLightIn);
                }
            }
            if (!geoModel.leftHandBones().isEmpty() && !RenderFixer.isCarryOnRender(offhandItem, bufferIn)) {
                if (SlashBladeCompat.isSlashBladeItem(offhandItem)) {
                    SlashBladeRender.renderMaidOffhandSlashBlade(geoModel, poseStack, bufferIn, packedLightIn, offhandItem);
                } else {
                    this.renderArmWithItem(entity, offhandItem, geoModel, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, bufferIn, packedLightIn);
                }
            }
            poseStack.popPose();
        }
    }

    @Override
    public GeoLayerMaidRender2<T, R> create(R geckoEntityMaidRenderer, EntityRendererProvider.Context renderManager, Function<Mob, IGeoEntity2> ysmGeoMob) {
        initYsmGeoMobGet(ysmGeoMob);
        return new GeckoLayerMaidHeld2<>(geckoEntityMaidRenderer, renderManager.getItemInHandRenderer());
    }
}
