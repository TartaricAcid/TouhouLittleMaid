package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class GeckoLayerMaidBackpack<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    public GeckoLayerMaidBackpack(R entityRendererIn) {
        super(entityRendererIn);
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> pModel, ResourceLocation pTextureLocation, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pEntity, float pRed, float pGreen, float pBlue) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(pTextureLocation));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F), pRed, pGreen, pBlue, 1.0F);
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBackpack<>(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null) {
            return;
        }
        ILocationModel model = getLocationModel(entity);
        if (model != null) {
            if (!getGeoEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }
            if (!model.backpackBones().isEmpty()) {
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, model.backpackBones());
                poseStack.translate(0, 1, 0.25);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                boolean showBackpack = entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack();
                IMaidBackpack backpack = showBackpack ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
                BackpackManager.findBackpackModel(backpack.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), poseStack, buffer, packedLight, maid, 1.0f, 1.0f, 1.0f));
                poseStack.popPose();
            }
        }
    }
}