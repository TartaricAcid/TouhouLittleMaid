package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class LayerMaidBanner extends RenderLayer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final EntityMaidRenderer renderer;
    private final MaidBannerModel bannerModel;

    public LayerMaidBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (stack.getItem() instanceof BannerItem bannerItem) {
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || mob.isSleeping() || mob.isInvisible()) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0, 0.5, 0.025);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Vector3f.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(matrixStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bannerModel.getBanner(), ModelBakery.BANNER_BASE, true, list);
            matrixStack.popPose();
        }
    }
}
