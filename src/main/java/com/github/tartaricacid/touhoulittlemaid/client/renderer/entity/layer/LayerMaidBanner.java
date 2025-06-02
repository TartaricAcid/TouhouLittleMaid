package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

public class LayerMaidBanner extends RenderLayer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
    private final EntityMaidRenderer renderer;
    private final SimpleBedrockModel<EntityMaid> bannerModel;

    public LayerMaidBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = Objects.requireNonNull(BedrockModelLoader.getModel(MAID_BANNER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (stack.getItem() instanceof BannerItem bannerItem) {
            if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping() || mob.isInvisible()) {
                return;
            }
            if (maid instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0, 0.5, 0.025);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
            this.bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            BannerPatternLayers patterns = maid.getBackpackShowItem().get(DataComponents.BANNER_PATTERNS);
            DyeColor dyeColor = ((AbstractBannerBlock) bannerItem.getBlock()).getColor();
            if (patterns != null) {
                renderPatterns(matrixStack, bufferIn, packedLightIn, bannerModel.getPart("banner"), patterns, dyeColor);
            }
            matrixStack.popPose();
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
