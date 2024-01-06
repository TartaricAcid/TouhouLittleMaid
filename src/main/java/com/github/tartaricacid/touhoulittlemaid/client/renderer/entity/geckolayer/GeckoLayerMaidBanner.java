package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class GeckoLayerMaidBanner<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final GeckoEntityMaidRenderer renderer;
    private final MaidBannerModel bannerModel;

    public GeckoLayerMaidBanner(GeckoEntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof EntityMaid maid && maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem) {
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                return;
            }
            GeoModel geoModel = this.entityRenderer.getGeoModel();
            if (geoModel != null && !geoModel.backpackBones.isEmpty()) {
                matrixStack.pushPose();
                translateToBackpack(matrixStack, geoModel);
                matrixStack.translate(0, -1.5, 0);
                matrixStack.scale(0.7F, 0.7F, 0.7F);
                matrixStack.mulPose(Axis.XN.rotationDegrees(5));
                VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
                bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(maid.getBackpackShowItem()));
                BannerRenderer.renderPatterns(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
                matrixStack.popPose();
            }
        }
    }

    private void translateToBackpack(PoseStack poseStack, GeoModel geoModel) {
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
