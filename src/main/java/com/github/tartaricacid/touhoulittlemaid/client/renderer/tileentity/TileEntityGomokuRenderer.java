package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.client.model.GomokuModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.PieceModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TileEntityGomokuRenderer implements BlockEntityRenderer<TileEntityGomoku> {
    private static final ResourceLocation CHECKER_BOARD_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/gomoku.png");
    private static final ResourceLocation BLACK_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/black_piece.png");
    private static final ResourceLocation WHITE_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/white_piece.png");
    private final GomokuModel CHECKER_BOARD_MODEL;
    private final PieceModel PIECE_MODEL;

    public TileEntityGomokuRenderer(BlockEntityRendererProvider.Context context) {
        CHECKER_BOARD_MODEL = new GomokuModel(context.bakeLayer(GomokuModel.LAYER));
        PIECE_MODEL = new PieceModel(context.bakeLayer(PieceModel.LAYER));
    }

    @Override
    public void render(TileEntityGomoku gomoku, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityTranslucent(CHECKER_BOARD_TEXTURE));
        CHECKER_BOARD_MODEL.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.translate(1.055, -0.1, -0.92);
        int[][] chessData = gomoku.getChessData();
        for (int[] row : chessData) {
            for (int j = 0; j < chessData[0].length; j++) {
                poseStack.translate(-0.1316, 0, 0);
                if (row[j] == Point.BLACK) {
                    VertexConsumer blackPieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(BLACK_PIECE_TEXTURE));
                    PIECE_MODEL.renderToBuffer(poseStack, blackPieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                if (row[j] == Point.WHITE) {
                    VertexConsumer whitePieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(WHITE_PIECE_TEXTURE));
                    PIECE_MODEL.renderToBuffer(poseStack, whitePieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
            poseStack.translate(1.974, 0, 0.1316);
        }
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityGomoku te) {
        return true;
    }
}
