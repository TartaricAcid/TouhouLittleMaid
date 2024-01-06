package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class LayerMaidBanner extends RenderLayer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final EntityMaidRenderer renderer;
    private final MaidBannerModel bannerModel;

    public LayerMaidBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = maid.getBackpackShowItem();
        if (stack.getItem() instanceof BannerItem bannerItem) {
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0, 0.25, 0);
            matrixStack.scale(0.7F, 0.7F, 0.7F);
            matrixStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
            matrixStack.popPose();
        }
    }
}
