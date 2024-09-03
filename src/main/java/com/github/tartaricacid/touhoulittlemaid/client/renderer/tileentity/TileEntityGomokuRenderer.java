package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.GomokuModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.PieceModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
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

public class TileEntityGomokuRenderer implements BlockEntityRenderer<TileEntityGomoku> {
    private static final ResourceLocation CHECKER_BOARD_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/gomoku.png");
    private static final ResourceLocation BLACK_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/black_piece.png");
    private static final ResourceLocation WHITE_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/white_piece.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private final GomokuModel CHECKER_BOARD_MODEL;
    private final PieceModel PIECE_MODEL;
    private final Font font;
    private final BlockEntityRenderDispatcher dispatcher;

    public TileEntityGomokuRenderer(BlockEntityRendererProvider.Context context) {
        CHECKER_BOARD_MODEL = new GomokuModel(context.bakeLayer(GomokuModel.LAYER));
        PIECE_MODEL = new PieceModel(context.bakeLayer(PieceModel.LAYER));
        this.font = context.getFont();
        this.dispatcher = context.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(TileEntityGomoku gomoku, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        this.renderChessboard(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        this.renderPiece(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        this.renderLatestChessTips(gomoku, poseStack, bufferIn, combinedLightIn);
        this.renderTipsText(gomoku, poseStack, bufferIn, combinedLightIn);
    }

    private void renderLatestChessTips(TileEntityGomoku gomoku, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn) {
        if (!gomoku.getLatestChessPoint().equals(Point.NULL) && inRenderDistance(gomoku, PIECE_RENDER_DISTANCE)) {
            Camera camera = this.dispatcher.camera;
            Point point = gomoku.getLatestChessPoint();
            poseStack.pushPose();
            poseStack.translate(-0.42, 0.25, -0.42);
            poseStack.translate(point.x * 0.1316, 0, point.y * 0.1316);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.scale(0.015625F, -0.015625F, 0.015625F);
            float width = (float) (-this.font.width("▼") / 2) + 0.5f;
            this.font.drawInBatch("▼", width, -1.5f, 0xFF0000, false, poseStack.last().pose(), bufferIn, false, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private void renderChessboard(TileEntityGomoku gomoku, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = gomoku.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(facing.get2DDataValue() * 90));
        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        }
        VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(CHECKER_BOARD_TEXTURE));
        CHECKER_BOARD_MODEL.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private void renderPiece(TileEntityGomoku gomoku, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
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
                        VertexConsumer blackPieceBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(BLACK_PIECE_TEXTURE));
                        PIECE_MODEL.renderToBuffer(poseStack, blackPieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    if (row[j] == Point.WHITE) {
                        VertexConsumer whitePieceBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(WHITE_PIECE_TEXTURE));
                        PIECE_MODEL.renderToBuffer(poseStack, whitePieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
                poseStack.translate(-0.1316, 0, -1.974);
            }
            poseStack.popPose();
        }
    }

    private void renderTipsText(TileEntityGomoku gomoku, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn) {
        if (!gomoku.isInProgress() && inRenderDistance(gomoku, TIPS_RENDER_DISTANCE)) {
            Camera camera = this.dispatcher.camera;
            MutableComponent loseTips;
            MutableComponent resetTips = Component.translatable("message.touhou_little_maid.gomoku.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
            MutableComponent roundText = Component.translatable("message.touhou_little_maid.gomoku.round", gomoku.getChessCounter()).withStyle(ChatFormatting.WHITE);
            MutableComponent preRoundIcon = Component.literal("⏹ ").withStyle(ChatFormatting.GREEN);
            MutableComponent postRoundIcon = Component.literal(" ⏹").withStyle(ChatFormatting.GREEN);
            MutableComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);
            if (gomoku.isPlayerTurn()) {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
            float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
            float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
            float roundTipsWidth = (float) (-this.font.width(roundTips) / 2);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.75, 0.5);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.mulPose(Vector3f.XN.rotationDegrees(camera.getXRot()));
            poseStack.scale(0.03F, -0.03F, 0.03F);
            this.font.drawInBatch(loseTips, loseTipsWidth, -10, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, false, 0, combinedLightIn);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.font.drawInBatch(roundTips, roundTipsWidth, -30, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, false, 0, combinedLightIn);
            this.font.drawInBatch(resetTips, resetTipsWidth, 0, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, false, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private boolean inRenderDistance(TileEntityGomoku gomoku, int distance) {
        BlockPos pos = gomoku.getBlockPos();
        return this.dispatcher.camera.getPosition().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityGomoku te) {
        return true;
    }
}
