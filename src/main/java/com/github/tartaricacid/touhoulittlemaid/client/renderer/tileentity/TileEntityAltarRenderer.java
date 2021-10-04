package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.AltarModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import static net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.GROUND;

public class TileEntityAltarRenderer extends TileEntityRenderer<TileEntityAltar> {
    private static final AltarModel MODEL = new AltarModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/altar.png");

    public TileEntityAltarRenderer(TileEntityRendererDispatcher render) {
        super(render);
    }

    @Override
    public void render(TileEntityAltar te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (te.isRender()) {
            matrixStack.pushPose();
            this.setTranslateAndPose(te, matrixStack);
            matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            IVertexBuilder buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            MODEL.renderToBuffer(matrixStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }

        if (te.isCanPlaceItem() && !te.handler.getStackInSlot(0).isEmpty()) {
            ItemStack stack = te.handler.getStackInSlot(0);
            matrixStack.pushPose();
            matrixStack.translate(0.5, 1.25, 0.5);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, GROUND, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityAltar te) {
        return true;
    }

    private void setTranslateAndPose(TileEntityAltar te, MatrixStack matrixStackIn) {
        switch (te.getDirection()) {
            case SOUTH:
                matrixStackIn.translate(1, -1.5, -3);
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
            case EAST:
                matrixStackIn.translate(-3, -1.5, 0);
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270));
                break;
            case WEST:
                matrixStackIn.translate(4, -1.5, 1);
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case NORTH:
            default:
                matrixStackIn.translate(0, -1.5, 4);
        }
    }
}
