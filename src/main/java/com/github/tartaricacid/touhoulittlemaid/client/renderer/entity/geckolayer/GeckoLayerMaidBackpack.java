package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class GeckoLayerMaidBackpack<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final GeckoEntityMaidRenderer renderer;

    public GeckoLayerMaidBackpack(GeckoEntityMaidRenderer entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> pModel, ResourceLocation pTextureLocation, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pEntity, float pRed, float pGreen, float pBlue) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(pTextureLocation));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F), pRed, pGreen, pBlue, 1.0F);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof EntityMaid maid && this.entityRenderer.getGeoModel() != null) {
            if (!renderer.getMainInfo().isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                return;
            }
            GeoModel geoModel = this.entityRenderer.getGeoModel();
            if (!geoModel.backpackBones.isEmpty()) {
                translateToBackpack(poseStack, geoModel);
                poseStack.translate(0, 1, 0.25);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                IMaidBackpack backpack = InGameMaidConfig.INSTANCE.isShowBackpack() ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
                BackpackManager.findBackpackModel(backpack.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f));
            }
        }
    }

    protected void translateToBackpack(PoseStack poseStack, GeoModel geoModel) {
        int size = geoModel.backpackBones.size();
        for (int i = 0; i < size - 1; i++) {
            RenderUtils.prepMatrixForBone(poseStack, geoModel.backpackBones.get(i));
        }
        GeoBone lastBone = geoModel.backpackBones.get(size - 1);
        RenderUtils.translateMatrixToBone(poseStack, lastBone);
        RenderUtils.translateToPivotPoint(poseStack, lastBone);
        RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
        RenderUtils.scaleMatrixForBone(poseStack, lastBone);
    }
}
