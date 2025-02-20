package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GeckoLayerMaidHeld<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private final ItemInHandRenderer itemInHandRenderer;

    public GeckoLayerMaidHeld(R entityRendererIn, ItemInHandRenderer itemInHandRenderer) {
        super(entityRendererIn);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidHeld<>(entityRendererIn, this.itemInHandRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack offhandItem = entity.getOffhandItem();
        ItemStack mainHandItem = entity.getMainHandItem();
        ILocationModel geoModel = getLocationModel(entity);
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

    protected void renderArmWithItem(T livingEntity, ItemStack itemStack, ILocationModel geoModel, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
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

    protected void translateToHand(HumanoidArm arm, PoseStack poseStack, ILocationModel geoModel) {
        if (arm == HumanoidArm.LEFT) {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.leftHandBones());
        } else {
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.rightHandBones());
        }
    }
}
