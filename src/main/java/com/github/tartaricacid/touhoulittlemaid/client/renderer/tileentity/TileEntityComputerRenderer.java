package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.ComputerModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityComputer;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class TileEntityComputerRenderer implements BlockEntityRenderer<TileEntityComputer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/computer.png");
    private final ComputerModel model;

    public TileEntityComputerRenderer(BlockEntityRendererProvider.Context context) {
        model = new ComputerModel(context.bakeLayer(ComputerModel.LAYER));
    }

    @Override
    public void render(TileEntityComputer computer, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = computer.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        model.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityComputer te) {
        return true;
    }
}
