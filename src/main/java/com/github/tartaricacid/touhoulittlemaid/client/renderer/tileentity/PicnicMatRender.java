package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockPicnicMat;
import com.github.tartaricacid.touhoulittlemaid.client.model.PicnicMatModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.BlockEntitySectionGeometryRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.SectionGeometryRenderContext;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;

public class PicnicMatRender implements BlockEntityRenderer<TileEntityPicnicMat>, BlockEntitySectionGeometryRenderer<TileEntityPicnicMat> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/picnic_mat.png");
    public static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    private final PicnicMatModel model;
    private final BlockEntityRendererProvider.Context context;

    public PicnicMatRender(BlockEntityRendererProvider.Context context) {
        this.model = new PicnicMatModel(context.bakeLayer(PicnicMatModel.LAYER));
        this.context = context;
    }

    @Override
    public void renderSectionGeometry(TileEntityPicnicMat picnicMat, AddSectionGeometryEvent.SectionRenderingContext context, PoseStack poseStack, BlockPos pos, BlockPos regionOrigin, SectionGeometryRenderContext renderAndCacheContext) {
        BlockState blockState = picnicMat.getBlockState();
        if (blockState.getValue(BlockPicnicMat.PART).isCenter()) {
            Direction facing = blockState.getValue(BlockPicnicMat.FACING);
            int combinedLightIn = renderAndCacheContext.getPackedLight();
            int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.mulPose(Axis.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
            {
                this.model.getHideModel().getChild("basketHide").visible = !picnicMat.isEmpty(0);
                this.model.getHideModel().getChild("breadHide").visible = !picnicMat.isEmpty(1);
                this.model.getHideModel().getChild("cakeHide").visible = !picnicMat.isEmpty(2);
            }
            {
                renderFood(picnicMat, 3, -0.6f, -1.5f, 1.4125f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 4, 0.15f, -1.2f, 1.4125f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 5, 0.55f, -1.6f, 1.4125f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);

                renderFood(picnicMat, 6, -0.5f, 1.65f, 1.4125f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 7, 0.375f, 1.575f, 1.4125f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);
                renderFood(picnicMat, 8, -0.05f, 1.2f, 1.25f, poseStack, renderAndCacheContext, combinedLightIn, combinedOverlayIn);
            }
            MultiBufferSource bufferIn = renderAndCacheContext.getUncachedDynamicCutoutBufferSource(TEXTURE);
            VertexConsumer buffer = bufferIn.getBuffer(RENDER_TYPE);
            model.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    @Override
    public void render(TileEntityPicnicMat picnicMat, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
    }

    private void renderFood(TileEntityPicnicMat picnicMat, int slotId, float x, float y, float z, PoseStack poseStack, SectionGeometryRenderContext renderAndCacheContext, int combinedLightIn, int combinedOverlayIn) {
        ItemStack storageItem = picnicMat.getStorageItem(slotId);
        if (!storageItem.isEmpty()) {
            int count = storageItem.getCount();
            poseStack.pushPose();
            poseStack.mulPose(Axis.XN.rotationDegrees(90));
            poseStack.translate(x, y, z);
            poseStack.scale(0.4f, 0.4f, 0.4f);
            renderAndCacheContext.renderUncachedItem(storageItem, ItemDisplayContext.FIXED, false, poseStack, combinedOverlayIn);
            if (count >= 10) {
                int stackCount = count / 10;
                for (int i = 0; i < stackCount; i++) {
                    poseStack.translate(Math.sin(i) * 0.05, Math.cos(i) * 0.03, -0.07);
                    poseStack.mulPose(Axis.ZN.rotationDegrees((float) Math.cos(i) * 60));
                    renderAndCacheContext.renderUncachedItem(storageItem, ItemDisplayContext.FIXED, false, poseStack, combinedOverlayIn);
                }
            }
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityPicnicMat te) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityPicnicMat te) {
        return RenderHelper.getAABB(te.getWorldPosition().offset(-3, 0, -3), te.getWorldPosition().offset(3, 1, 3));
    }
}
