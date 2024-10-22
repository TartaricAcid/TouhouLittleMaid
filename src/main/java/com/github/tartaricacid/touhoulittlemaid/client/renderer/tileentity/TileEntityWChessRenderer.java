package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.WChessModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.WChessPiecesModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityWChess;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
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

public class TileEntityWChessRenderer implements BlockEntityRenderer<TileEntityWChess> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/wchess.png");
    private static final ResourceLocation PIECES_TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/wchess_pieces.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private final Font font;
    private final BlockEntityRenderDispatcher dispatcher;
    private final WChessModel chessModel;
    private final WChessPiecesModel[] chessPiecesModels;
    private final WChessPiecesModel selectedModels;

    public TileEntityWChessRenderer(BlockEntityRendererProvider.Context context) {
        chessModel = new WChessModel(context.bakeLayer(WChessModel.LAYER));
        chessPiecesModels = WChessPiecesModel.initModel(context.bakeLayer(WChessPiecesModel.LAYER));
        selectedModels = WChessPiecesModel.getSelectedModel(context.bakeLayer(WChessPiecesModel.LAYER));
        dispatcher = context.getBlockEntityRenderDispatcher();
        font = context.getFont();
    }

    @Override
    public void render(TileEntityWChess cchess, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = cchess.getBlockState().getValue(BlockGomoku.FACING);
        this.renderChessboard(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing);
        this.renderPiece(cchess, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing);
        this.renderTipsText(cchess, poseStack, bufferIn, combinedLightIn);
    }

    private void renderTipsText(TileEntityWChess chess, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn) {
        boolean showTips = chess.isCheckmate() || chess.isRepeat() || chess.isMoveNumberLimit();
        if (!showTips || !inRenderDistance(chess, TIPS_RENDER_DISTANCE)) {
            return;
        }

        Camera camera = this.dispatcher.camera;
        MutableComponent loseTips = null;
        MutableComponent resetTips = Component.translatable("message.touhou_little_maid.wchess.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
        MutableComponent roundText = Component.translatable("message.touhou_little_maid.gomoku.round", chess.getChessCounter()).withStyle(ChatFormatting.WHITE);
        MutableComponent preRoundIcon = Component.literal("⏹ ").withStyle(ChatFormatting.GREEN);
        MutableComponent postRoundIcon = Component.literal(" ⏹").withStyle(ChatFormatting.GREEN);
        MutableComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);

        if (chess.isCheckmate()) {
            if (!chess.isPlayerTurn()) {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
        } else if (chess.isMoveNumberLimit()) {
            loseTips = Component.translatable("message.touhou_little_maid.cchess.move_limit").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
        } else if (chess.isRepeat()) {
            loseTips = Component.translatable("message.touhou_little_maid.cchess.repeat").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
        }
        if (loseTips == null) {
            return;
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

    private void renderPiece(TileEntityWChess cchess, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, Direction facing) {
        if (inRenderDistance(cchess, PIECE_RENDER_DISTANCE)) {
            VertexConsumer piecesBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(PIECES_TEXTURE));
            int selectX = Position.FILE_X(cchess.getSelectChessPoint());
            int selectY = Position.RANK_Y(cchess.getSelectChessPoint());
            byte[] data = cchess.getChessData().squares;
            poseStack.pushPose();
            switch (facing) {
                case NORTH:
                    poseStack.translate(0.875 + 0.5, 1.625, 0.875 + 0.5);
                    break;
                case EAST:
                    poseStack.translate(-0.875 + 0.5, 1.625, 0.875 + 0.5);
                    break;
                case WEST:
                    poseStack.translate(0.875 + 0.5, 1.625, -0.875 + 0.5);
                    break;
                default:
                    poseStack.translate(-0.875 + 0.5, 1.625, -0.875 + 0.5);
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
                    if (WChessUtil.isWhite(piecesIndex) || WChessUtil.isBlack(piecesIndex)) {
                        WChessPiecesModel chessPiecesModel = this.chessPiecesModels[piecesIndex];
                        chessPiecesModel.renderToBuffer(poseStack, piecesBuff, combinedLightIn, combinedOverlayIn);
                        if (selectX == x && selectY == y) {
                            selectedModels.renderToBuffer(poseStack, piecesBuff, combinedLightIn, combinedOverlayIn);
                        }
                    }
                    poseStack.translate(0.25, 0, 0);
                }
                poseStack.translate(-0.25 * 8, 0, -0.25);
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
        chessModel.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn);
        poseStack.popPose();
    }

    private boolean inRenderDistance(TileEntityWChess chess, int distance) {
        BlockPos pos = chess.getBlockPos();
        return this.dispatcher.camera.getPosition().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityWChess te) {
        return true;
    }
}
