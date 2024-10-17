package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.CChessModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.CChessPiecesModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityCChess;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class TileEntityCChessRenderer implements BlockEntityRenderer<TileEntityCChess> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/cchess.png");
    private static final ResourceLocation PIECES_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/cchess_pieces.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private final Font font;
    private final BlockEntityRenderDispatcher dispatcher;
    private final CChessModel chessModel;
    private final CChessPiecesModel[] chessPiecesModels;
    private final CChessPiecesModel selectedModels;

    public TileEntityCChessRenderer(BlockEntityRendererProvider.Context context) {
        chessModel = new CChessModel(context.bakeLayer(CChessModel.LAYER));
        chessPiecesModels = CChessPiecesModel.initModel(context);
        selectedModels = CChessPiecesModel.getSelectedModel(context);
        dispatcher = context.getBlockEntityRenderDispatcher();
        font = context.getFont();
    }

    @Override
    public void render(TileEntityCChess cchess, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = cchess.getBlockState().getValue(BlockGomoku.FACING);
        this.renderChessboard(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing);
        this.renderPiece(cchess, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing);
        this.renderTipsText(cchess, poseStack, bufferIn, combinedLightIn);
    }

    private void renderTipsText(TileEntityCChess chess, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn) {
        if (chess.isCheckmate() && inRenderDistance(chess, TIPS_RENDER_DISTANCE)) {
            Camera camera = this.dispatcher.camera;
            MutableComponent loseTips;
            MutableComponent resetTips = Component.translatable("message.touhou_little_maid.gomoku.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
            MutableComponent roundText = Component.translatable("message.touhou_little_maid.gomoku.round", chess.getChessCounter()).withStyle(ChatFormatting.WHITE);
            MutableComponent preRoundIcon = Component.literal("⏹ ").withStyle(ChatFormatting.GREEN);
            MutableComponent postRoundIcon = Component.literal(" ⏹").withStyle(ChatFormatting.GREEN);
            MutableComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);
            if (!chess.isPlayerTurn()) {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
            float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
            float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
            float roundTipsWidth = (float) (-this.font.width(roundTips) / 2);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.75, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.mulPose(Axis.XN.rotationDegrees(camera.getXRot()));
            poseStack.scale(0.03F, -0.03F, 0.03F);
            this.font.drawInBatch(loseTips, loseTipsWidth, -10, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, Font.DisplayMode.POLYGON_OFFSET, 0, combinedLightIn);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.font.drawInBatch(roundTips, roundTipsWidth, -30, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, Font.DisplayMode.POLYGON_OFFSET, 0, combinedLightIn);
            this.font.drawInBatch(resetTips, resetTipsWidth, 0, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, Font.DisplayMode.POLYGON_OFFSET, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private void renderPiece(TileEntityCChess cchess, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, Direction facing) {
        if (inRenderDistance(cchess, PIECE_RENDER_DISTANCE)) {
            VertexConsumer piecesBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(PIECES_TEXTURE));
            int selectX = Position.FILE_X(cchess.getSelectChessPoint());
            int selectY = Position.RANK_Y(cchess.getSelectChessPoint());
            byte[] data = cchess.getChessData().squares;
            poseStack.pushPose();
            switch (facing) {
                case NORTH:
                    poseStack.translate(1.365 + 0.5, 1.625, 1.370 + 0.5);
                    break;
                case EAST:
                    poseStack.translate(-1.365 + 0.5, 1.625, 1.370 + 0.5);
                    break;
                case WEST:
                    poseStack.translate(1.365 + 0.5, 1.625, -1.370 + 0.5);
                    break;
                default:
                    poseStack.translate(-1.365 + 0.5, 1.625, -1.370 + 0.5);
                    break;
            }
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
            if (facing == Direction.SOUTH || facing == Direction.NORTH) {
                poseStack.mulPose(Axis.YN.rotationDegrees(180));
            }
            for (int y = Position.RANK_TOP; y <= Position.RANK_BOTTOM; y++) {
                for (int x = Position.FILE_LEFT; x <= Position.FILE_RIGHT; x++) {
                    byte piecesIndex = data[Position.COORD_XY(x, y)];
                    if (CChessUtil.isRed(piecesIndex) || CChessUtil.isBlack(piecesIndex)) {
                        CChessPiecesModel chessPiecesModel = this.chessPiecesModels[piecesIndex];
                        chessPiecesModel.renderToBuffer(poseStack, piecesBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                        if (selectX == x && selectY == y) {
                            selectedModels.renderToBuffer(poseStack, piecesBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                        }
                    }
                    poseStack.translate(0.304, 0, 0);
                }
                poseStack.translate(-0.304 * 9, 0, -0.304);
            }
            poseStack.popPose();
        }
    }

    private void renderChessboard(PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, Direction facing) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
        }
        VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        chessModel.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private boolean inRenderDistance(TileEntityCChess chess, int distance) {
        BlockPos pos = chess.getBlockPos();
        return this.dispatcher.camera.getPosition().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityCChess te) {
        return true;
    }
}
