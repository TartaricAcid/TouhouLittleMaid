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
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

public class LayerMaidBanner extends RenderLayer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");
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
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(stack));
            renderPatterns(matrixStack, bufferIn, packedLightIn, bannerModel.getPart("banner"), list);
            matrixStack.popPose();
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
