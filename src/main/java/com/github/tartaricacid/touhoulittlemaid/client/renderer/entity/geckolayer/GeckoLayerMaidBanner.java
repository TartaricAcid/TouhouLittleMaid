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
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
    private final SimpleBedrockModel<EntityMaid> bannerModel;
    private final EntityModelSet modelSet;
    private final ModelPart flag;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.bannerModel = Objects.requireNonNull(BedrockModelLoader.getModel(MAID_BANNER));
        this.flag = modelSet.bakeLayer(ModelLayers.BANNER).getChild("flag");
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
            this.bannerModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            BannerPatternLayers patterns = maid.getBackpackShowItem().get(DataComponents.BANNER_PATTERNS);
            DyeColor dyeColor = ((AbstractBannerBlock) bannerItem.getBlock()).getColor();
            if (patterns != null) {
                renderPatterns(poseStack, bufferIn, packedLight, bannerModel.getPart("banner"), patterns, dyeColor);
            }
            poseStack.popPose();
        }
    }

    private void renderPatterns(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                BedrockPart banner, BannerPatternLayers patterns, DyeColor dyeColor) {
        banner.render(poseStack, ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid, false),
                packedLight, OverlayTexture.NO_OVERLAY);
        renderPatternLayer(poseStack, bufferSource, packedLight, banner, Sheets.BANNER_BASE, dyeColor);
        for (int i = 0; i < 16 && i < patterns.layers().size(); ++i) {
            BannerPatternLayers.Layer layer = patterns.layers().get(i);
            Material material = Sheets.getBannerMaterial(layer.pattern());
            renderPatternLayer(poseStack, bufferSource, packedLight, banner, material, layer.color());
        }
    }

    private void renderPatternLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BedrockPart banner, Material material, DyeColor color) {
        int packedColor = color.getTextureDiffuseColor();
        float red = FastColor.ARGB32.red(packedColor) / 255f;
        float green = FastColor.ARGB32.green(packedColor) / 255f;
        float blue = FastColor.ARGB32.blue(packedColor) / 255f;
        banner.render(poseStack, material.buffer(buffer, RenderType::entityNoOutline), packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}
