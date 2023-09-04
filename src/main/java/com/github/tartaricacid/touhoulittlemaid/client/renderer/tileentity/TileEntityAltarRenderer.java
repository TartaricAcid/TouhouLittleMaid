package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.AltarModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TileEntityAltarRenderer implements BlockEntityRenderer<TileEntityAltar> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/altar.png");
    private final AltarModel MODEL;

    public TileEntityAltarRenderer(BlockEntityRendererProvider.Context render) {
        MODEL = new AltarModel(render.bakeLayer(AltarModel.LAYER));
    }

    @Override
    public void render(TileEntityAltar te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (te.isRender()) {
            poseStack.pushPose();
            this.setTranslateAndPose(te, poseStack);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            MODEL.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }

        if (te.isCanPlaceItem() && !te.handler.getStackInSlot(0).isEmpty()) {
            ItemStack stack = te.handler.getStackInSlot(0);
            poseStack.pushPose();
            poseStack.translate(0.5, 1.25, 0.5);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, combinedOverlayIn, poseStack, bufferIn, te.getLevel(), 0);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityAltar te) {
        return true;
    }

    private void setTranslateAndPose(TileEntityAltar te, PoseStack poseStack) {
        switch (te.getDirection()) {
            case SOUTH:
                poseStack.translate(1, -1.5, -3);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                poseStack.translate(-3, -1.5, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case WEST:
                poseStack.translate(4, -1.5, 1);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case NORTH:
            default:
                poseStack.translate(0, -1.5, 4);
        }
    }
}
