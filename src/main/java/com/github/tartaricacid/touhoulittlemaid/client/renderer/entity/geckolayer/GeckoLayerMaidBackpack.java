package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Function;

public class GeckoLayerMaidBackpack<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerMaidRender<T, R> {

    public GeckoLayerMaidBackpack(R entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> pModel, ResourceLocation pTextureLocation, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pEntity, float pRed, float pGreen, float pBlue) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(pTextureLocation));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F), pRed, pGreen, pBlue, 1.0F);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null) {
            return;
        }
        IAnimatedModel<?> model = getGeoMobModel(maid);
        if (model != null) {
            if (!getGeoMob(maid).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
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

    public void ysmRender(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null) {
            return;
        }
        IAnimatedModel<?> model = getYsmGeoMobModel(maid);
        if (model != null) {
            if (!getYsmGeoMob(maid).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }
            if (!model.bones().isEmpty()) {
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, model.backpackBones());
                poseStack.translate(0, 1, 0.25);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                boolean showBackpack = entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack();
                IMaidBackpack backpack = showBackpack ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
                BackpackManager.findBackpackModel(backpack.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f));
                poseStack.popPose();
            }
        }
    }

    @Override
    public GeoLayerMaidRender<T, R> create(R geckoEntityMaidRenderer, EntityRendererProvider.Context renderManager, Function<Mob, IGeoEntity> ysmGeoMob) {
        initYsmGeoMobGet(ysmGeoMob);
        return new GeckoLayerMaidBackpack<>(geckoEntityMaidRenderer, renderManager.getModelSet());
    }
}