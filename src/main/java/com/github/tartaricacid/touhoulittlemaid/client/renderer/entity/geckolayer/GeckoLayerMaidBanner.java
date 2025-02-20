package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final MaidBannerModel bannerModel;
    private final EntityModelSet modelSet;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new GeckoLayerMaidBanner<>(entityRendererIn, modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null || !(maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        if (!getGeoEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
            return;
        }
        if (!maid.getConfigManager().isShowBackItem()) {
            return;
        }
        ILocationModel geoModel = getLocationModel(entity);
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
}
