package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.TombstoneModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class EntityTombstoneRenderer extends EntityRenderer<EntityTombstone> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone/tombstone.png");
    private static final ResourceLocation THE_NETHER_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone/tombstone_the_nether.png");
    private static final ResourceLocation THE_END_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone/tombstone_the_end.png");
    private static final ResourceLocation TWILIGHT_FOREST_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone/tombstone_twilight_forest.png");
    private static final ResourceLocation AETHER_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone/tombstone_aether.png");

    private static final ResourceLocation TWILIGHT_FOREST_LEVEL_ID = new ResourceLocation("twilightforest", "twilight_forest");
    private final static ResourceLocation AETHER_LEVEL_ID = new ResourceLocation("aether", "the_aether");

    private static final int NAME_SHOW_DISTANCE = 64;
    private final TombstoneModel tombstoneModel;

    public EntityTombstoneRenderer(EntityRendererProvider.Context manager) {
        super(manager);
        tombstoneModel = new TombstoneModel(manager.bakeLayer(TombstoneModel.LAYER));
    }

    @Override
    public void render(EntityTombstone tombstone, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0, -1.501, 0.0);
        tombstoneModel.setupAnim(tombstone, 0, 0, -0.1f, 0, 0);
        RenderType renderType = RenderType.entityCutoutNoCull(getTextureLocation(tombstone));
        VertexConsumer buffer = bufferIn.getBuffer(renderType);
        tombstoneModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        if (this.shouldShowName(tombstone)) {
            this.renderNameTag(tombstone, new TranslatableComponent("entity.touhou_little_maid.tombstone.display").withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE), 1.6f, poseStack, bufferIn, packedLight);
            this.renderNameTag(tombstone, tombstone.getMaidName(), 1.85f, poseStack, bufferIn, packedLight);
        }
    }

    @Override
    protected boolean shouldShowName(EntityTombstone tombstone) {
        return !tombstone.getMaidName().equals(TextComponent.EMPTY);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTombstone entity) {
        ResourceLocation dimension = entity.level.dimension().location();
        if (dimension.equals(Level.NETHER.location())) {
            return THE_NETHER_TEXTURE;
        }
        if (dimension.equals(Level.END.location())) {
            return THE_END_TEXTURE;
        }
        if (dimension.equals(TWILIGHT_FOREST_LEVEL_ID)) {
            return TWILIGHT_FOREST_TEXTURE;
        }
        if (dimension.equals(AETHER_LEVEL_ID)) {
            return AETHER_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }

    private void renderNameTag(EntityTombstone tombstone, Component component, float yOffset, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        double distance = this.entityRenderDispatcher.distanceToSqr(tombstone);
        if (distance < (NAME_SHOW_DISTANCE * NAME_SHOW_DISTANCE)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, yOffset, 0.0F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            Font font = this.getFont();
            float width = (float) (-font.width(component) / 2);
            font.drawInBatch(component, width, 0, -1, false, matrix4f, bufferSource, false, 0, packedLight);
            poseStack.popPose();
        }
    }
}
