package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
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

public class GeckoLayerMaidBanner<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final MaidBannerModel bannerModel;

    public GeckoLayerMaidBanner(GeckoEntityMaidRenderer<T> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }
        if (maid.getBackpackShowItem().getItem() instanceof BannerItem bannerItem) {
            if (!this.entityRenderer.getAnimatableEntity(entity).getMaidInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }
            AnimatedGeoModel geoModel = this.entityRenderer.getAnimatableEntity(entity).getCurrentModel();
            if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
                matrixStack.pushPose();
                RenderUtils.prepMatrixForLocator(matrixStack, geoModel.backpackBones());
                matrixStack.translate(0, -1.5, 0.02);
                matrixStack.scale(0.65F, 0.65F, 0.65F);
                matrixStack.mulPose(Axis.XN.rotationDegrees(5));
                VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
                this.bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(maid.getBackpackShowItem()));
                BannerRenderer.renderPatterns(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, this.bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
                matrixStack.popPose();
            }
        }
    }
}
