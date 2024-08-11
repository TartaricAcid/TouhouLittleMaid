package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class GeckoLayerMaidHeld<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public GeckoLayerMaidHeld(GeckoEntityMaidRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        this.itemInHandRenderer = Minecraft.getInstance().getItemInHandRenderer();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack offhandItem = entityLivingBaseIn.getOffhandItem();
        ItemStack mainHandItem = entityLivingBaseIn.getMainHandItem();
        AnimatedGeoModel geoModel = this.entityRenderer.getAnimatableEntity(entityLivingBaseIn).getCurrentModel();
        if (geoModel == null) {
            return;
        }
        if (!offhandItem.isEmpty() || !mainHandItem.isEmpty()) {
            poseStack.pushPose();
            if (!geoModel.rightHandBones().isEmpty() && !RenderFixer.isCarryOnRender(mainHandItem, bufferIn)) {
                if (SlashBladeCompat.isSlashBladeItem(mainHandItem)) {
                    SlashBladeRender.renderMaidMainhandSlashBlade(entityLivingBaseIn, geoModel, poseStack, bufferIn, packedLightIn, mainHandItem, partialTicks);
                } else {
                    this.renderArmWithItem(entityLivingBaseIn, mainHandItem, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, bufferIn, packedLightIn);
                }
            }
            if (!geoModel.leftHandBones().isEmpty() && !RenderFixer.isCarryOnRender(offhandItem, bufferIn)) {
                if (SlashBladeCompat.isSlashBladeItem(offhandItem)) {
                    SlashBladeRender.renderMaidOffhandSlashBlade(geoModel, poseStack, bufferIn, packedLightIn, offhandItem);
                } else {
                    this.renderArmWithItem(entityLivingBaseIn, offhandItem, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, bufferIn, packedLightIn);
                }
            }
            poseStack.popPose();
        }
    }

    protected void renderArmWithItem(T livingEntity, ItemStack itemStack, ItemTransforms.TransformType displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        AnimatedGeoModel geoModel = this.entityRenderer.getAnimatableEntity(livingEntity).getCurrentModel();
        if (!itemStack.isEmpty() && geoModel != null) {
            poseStack.pushPose();
            translateToHand(arm, poseStack, geoModel);
            poseStack.translate(0, -0.0625, -0.1);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean isLeftHand = arm == HumanoidArm.LEFT;
            this.itemInHandRenderer.renderItem(livingEntity, itemStack, displayContext, isLeftHand, poseStack, bufferSource, light);
            poseStack.popPose();
        }
    }

    protected void translateToHand(HumanoidArm arm, PoseStack poseStack, AnimatedGeoModel geoModel) {
        if (arm == HumanoidArm.LEFT) {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.leftHandBones());
        } else {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.rightHandBones());
        }
    }
}
