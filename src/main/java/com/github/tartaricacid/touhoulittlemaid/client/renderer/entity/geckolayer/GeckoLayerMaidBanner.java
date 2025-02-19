package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;
import java.util.function.Function;

public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerMaidRender<T, R> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final MaidBannerModel bannerModel;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null || !(maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        if (!getGeoMob(maid).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
            return;
        }
        if (!maid.getConfigManager().isShowBackItem()) {
            return;
        }
        IAnimatedModel geoModel = getGeoMobModel(maid);
        if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
            poseStack.pushPose();
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.backpackBones());
            poseStack.translate(0, 0.75, 0.3);
            poseStack.scale(0.65F, -0.65F, -0.65F);
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
            this.bannerModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(maid.getBackpackShowItem()));
            BannerRenderer.renderPatterns(poseStack, bufferIn, packedLight, OverlayTexture.NO_OVERLAY, this.bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
            poseStack.popPose();
        }
    }

    @Override
    public void ysmRender(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null || !(maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        if (!getYsmGeoMob(maid).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
            return;
        }
        if (!maid.getConfigManager().isShowBackItem()) {
            return;
        }
        IAnimatedModel geoModel = getYsmGeoMobModel(maid);
        if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
            poseStack.pushPose();
            RenderUtils.prepMatrixForLocator(poseStack, geoModel.backpackBones());
            poseStack.translate(0, 0.75, 0.3);
            poseStack.scale(0.65F, -0.65F, -0.65F);
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
            this.bannerModel.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(maid.getBackpackShowItem()));
            BannerRenderer.renderPatterns(poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, this.bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
            poseStack.popPose();
        }
    }

    @Override
    public GeoLayerMaidRender<T, R> create(R geckoEntityMaidRenderer, EntityRendererProvider.Context renderManager, Function<Mob, IGeoEntity> ysmGeoMob) {
        initYsmGeoMobGet(ysmGeoMob);
        return new GeckoLayerMaidBanner<>(geckoEntityMaidRenderer, renderManager.getModelSet());
    }
}
