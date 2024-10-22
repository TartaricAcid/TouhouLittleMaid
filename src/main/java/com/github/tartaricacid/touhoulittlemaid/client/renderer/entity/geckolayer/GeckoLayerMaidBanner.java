package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class GeckoLayerMaidBanner<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
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
            if (!this.entityRenderer.getAnimatableEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }
            if (entity instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
                return;
            }
            AnimatedGeoModel geoModel = this.entityRenderer.getAnimatableEntity(entity).getCurrentModel();
            if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
                matrixStack.pushPose();
                RenderUtils.prepMatrixForLocator(matrixStack, geoModel.backpackBones());
                matrixStack.translate(0, 0.75, 0.3);
                matrixStack.scale(0.65F, -0.65F, -0.65F);
                matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                matrixStack.mulPose(Axis.XN.rotationDegrees(5));
                VertexConsumer buffer = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
                this.bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                BannerPatternLayers bannerpatternlayers = maid.getBackpackShowItem().get(DataComponents.BANNER_PATTERNS);
                DyeColor baseColor = ((AbstractBannerBlock) bannerItem.getBlock()).getColor();
                if (bannerpatternlayers != null) {
                    BannerRenderer.renderPatterns(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, this.bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, baseColor, bannerpatternlayers);
                }
                matrixStack.popPose();
            }
        }
    }
}
