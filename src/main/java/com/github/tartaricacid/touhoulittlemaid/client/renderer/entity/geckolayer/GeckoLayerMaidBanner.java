package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
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
import net.minecraft.client.renderer.Sheets;
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
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
    private final SimpleBedrockModel<EntityMaid> bannerModel;
    private final EntityModelSet modelSet;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.bannerModel = Objects.requireNonNull(BedrockModelLoader.getModel(MAID_BANNER));
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
            renderPatterns(poseStack, bufferIn, packedLight, bannerModel.getPart("banner"), list);
            poseStack.popPose();
        }
    }

    private void renderPatterns(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                BedrockPart banner, List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {
        banner.render(poseStack, ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid, false),
                packedLight, OverlayTexture.NO_OVERLAY);
        for (int index = 0; index < 17 && index < patterns.size(); ++index) {
            Pair<Holder<BannerPattern>, DyeColor> patternPair = patterns.get(index);
            float[] colorComponents = patternPair.getSecond().getTextureDiffuseColors();
            patternPair.getFirst().unwrapKey().map(Sheets::getBannerMaterial).ifPresent(
                    material -> banner.render(poseStack, material.buffer(bufferSource, RenderType::entityNoOutline),
                            packedLight, OverlayTexture.NO_OVERLAY, colorComponents[0], colorComponents[1], colorComponents[2],
                            1.0F));
        }
    }
}
