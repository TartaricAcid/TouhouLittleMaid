package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.TombstoneModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EntityTombstoneRenderer extends EntityRenderer<EntityTombstone> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone.png");
    private static final ResourceLocation THE_NETHER_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone_the_nether.png");
    private static final ResourceLocation THE_END_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone_the_end.png");
    private static final ResourceLocation TWILIGHT_FOREST_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tombstone_twilight_forest.png");
    private static final ResourceLocation TWILIGHT_FOREST_NAME = new ResourceLocation("twilightforest", "twilight_forest");
    private static final int NAME_SHOW_DISTANCE = 64;
    private final TombstoneModel tombstoneModel;

    public EntityTombstoneRenderer(EntityRendererManager manager) {
        super(manager);
        tombstoneModel = new TombstoneModel();
    }

    @Override
    public void render(EntityTombstone tombstone, float entityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0, -1.501, 0.0);
        tombstoneModel.setupAnim(tombstone, 0, 0, -0.1f, 0, 0);
        RenderType renderType = RenderType.entityTranslucent(getTextureLocation(tombstone));
        IVertexBuilder buffer = bufferIn.getBuffer(renderType);
        tombstoneModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        if (this.shouldShowName(tombstone)) {
            this.renderNameTag(tombstone, new TranslationTextComponent("entity.touhou_little_maid.tombstone.display").withStyle(TextFormatting.GOLD, TextFormatting.UNDERLINE), 1.6f, poseStack, bufferIn, packedLight);
            this.renderNameTag(tombstone, tombstone.getMaidName(), 1.85f, poseStack, bufferIn, packedLight);
        }
    }

    @Override
    protected boolean shouldShowName(EntityTombstone tombstone) {
        return !tombstone.getMaidName().equals(StringTextComponent.EMPTY);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTombstone entity) {
        ResourceLocation dimension = entity.level.dimension().location();
        if (dimension.equals(World.NETHER.location())) {
            return THE_NETHER_TEXTURE;
        }
        if (dimension.equals(World.END.location())) {
            return THE_END_TEXTURE;
        }
        if (dimension.equals(TWILIGHT_FOREST_NAME)) {
            return TWILIGHT_FOREST_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }

    private void renderNameTag(EntityTombstone tombstone, ITextComponent component, float yOffset, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight) {
        double distance = this.entityRenderDispatcher.distanceToSqr(tombstone);
        if (distance < (NAME_SHOW_DISTANCE * NAME_SHOW_DISTANCE)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, yOffset, 0.0F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            FontRenderer font = this.getFont();
            float width = (float) (-font.width(component) / 2);
            font.drawInBatch(component, width, 0, -1, false, matrix4f, bufferSource, false, 0, packedLight);
            poseStack.popPose();
        }
    }
}
