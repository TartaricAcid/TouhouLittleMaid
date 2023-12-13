package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityGomokuRenderer extends TileEntityRenderer<TileEntityGomoku> {
    private static final ResourceLocation CHECKER_BOARD_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/gomoku.png");
    private static final ResourceLocation BLACK_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/black_piece.png");
    private static final ResourceLocation WHITE_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/white_piece.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private final GomokuModel CHECKER_BOARD_MODEL = new GomokuModel();
    private final PieceModel PIECE_MODEL = new PieceModel();
    private final FontRenderer font;

    public TileEntityGomokuRenderer(TileEntityRendererDispatcher context) {
        super(context);
        this.font = context.getFont();
    }

    @Override
    public void render(TileEntityGomoku gomoku, float partialTick, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        this.renderChessboard(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        this.renderPiece(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        this.renderLatestChessTips(gomoku, poseStack, bufferIn, combinedLightIn);
        this.renderTipsText(gomoku, poseStack, bufferIn, combinedLightIn);
    }

    private void renderLatestChessTips(TileEntityGomoku gomoku, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int combinedLightIn) {
        if (!gomoku.getLatestChessPoint().equals(Point.NULL) && inRenderDistance(gomoku, PIECE_RENDER_DISTANCE)) {
            ActiveRenderInfo camera = this.renderer.camera;
            Point point = gomoku.getLatestChessPoint();
            poseStack.pushPose();
            poseStack.translate(-0.42, 0.25, -0.42);
            poseStack.translate(point.x * 0.1316, 0, point.y * 0.1316);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.scale(0.015625F, -0.015625F, 0.015625F);
            float width = (float) (-this.font.width("▼") / 2) + 0.5f;
            this.font.drawInBatch("▼", width, -1.5f, 0xFF0000, false, poseStack.last().pose(), bufferIn, true, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private void renderChessboard(TileEntityGomoku gomoku, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = gomoku.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(facing.get2DDataValue() * 90));
        IVertexBuilder checkerBoardBuff = bufferIn.getBuffer(RenderType.entityTranslucent(CHECKER_BOARD_TEXTURE));
        CHECKER_BOARD_MODEL.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private void renderPiece(TileEntityGomoku gomoku, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (inRenderDistance(gomoku, PIECE_RENDER_DISTANCE)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            poseStack.translate(0.92, -0.1, -1.055);
            int[][] chessData = gomoku.getChessData();
            for (int[] row : chessData) {
                for (int j = 0; j < chessData[0].length; j++) {
                    poseStack.translate(0, 0, 0.1316);
                    if (row[j] == Point.BLACK) {
                        IVertexBuilder blackPieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(BLACK_PIECE_TEXTURE));
                        PIECE_MODEL.renderToBuffer(poseStack, blackPieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    if (row[j] == Point.WHITE) {
                        IVertexBuilder whitePieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(WHITE_PIECE_TEXTURE));
                        PIECE_MODEL.renderToBuffer(poseStack, whitePieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
                poseStack.translate(-0.1316, 0, -1.974);
            }
            poseStack.popPose();
        }
    }

    private void renderTipsText(TileEntityGomoku gomoku, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int combinedLightIn) {
        if (!gomoku.isInProgress() && inRenderDistance(gomoku, TIPS_RENDER_DISTANCE)) {
            ActiveRenderInfo camera = this.renderer.camera;
            IFormattableTextComponent loseTips;
            IFormattableTextComponent resetTips = new TranslationTextComponent("message.touhou_little_maid.gomoku.reset").withStyle(TextFormatting.UNDERLINE).withStyle(TextFormatting.AQUA);
            IFormattableTextComponent roundText = new TranslationTextComponent("message.touhou_little_maid.gomoku.round", gomoku.getChessCounter()).withStyle(TextFormatting.WHITE);
            IFormattableTextComponent preRoundIcon = new StringTextComponent("⏹ ").withStyle(TextFormatting.GREEN);
            IFormattableTextComponent postRoundIcon = new StringTextComponent(" ⏹").withStyle(TextFormatting.GREEN);
            IFormattableTextComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);
            if (gomoku.isPlayerTurn()) {
                loseTips = new TranslationTextComponent("message.touhou_little_maid.gomoku.win").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.DARK_PURPLE);
            } else {
                loseTips = new TranslationTextComponent("message.touhou_little_maid.gomoku.lose").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.DARK_PURPLE);
            }
            float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
            float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
            float roundTipsWidth = (float) (-this.font.width(roundTips) / 2);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.75, 0.5);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.mulPose(Vector3f.XN.rotationDegrees(camera.getXRot()));
            poseStack.scale(0.03F, -0.03F, 0.03F);
            this.font.drawInBatch(loseTips, loseTipsWidth, -10, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, true, 0, combinedLightIn);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.font.drawInBatch(roundTips, roundTipsWidth, -30, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, true, 0, combinedLightIn);
            this.font.drawInBatch(resetTips, resetTipsWidth, 0, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, true, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private boolean inRenderDistance(TileEntityGomoku gomoku, int distance) {
        BlockPos pos = gomoku.getBlockPos();
        return this.renderer.camera.getPosition().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityGomoku te) {
        return true;
    }
}
