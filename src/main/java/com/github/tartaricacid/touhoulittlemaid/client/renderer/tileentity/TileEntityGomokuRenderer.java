package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.GomokuModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.PieceModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
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

public class TileEntityGomokuRenderer implements BlockEntityRenderer<TileEntityGomoku> {
    private static final ResourceLocation CHECKER_BOARD_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/gomoku.png");
    private static final ResourceLocation BLACK_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/black_piece.png");
    private static final ResourceLocation WHITE_PIECE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/white_piece.png");
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
        Direction facing = gomoku.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
        VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityTranslucent(CHECKER_BOARD_TEXTURE));
        CHECKER_BOARD_MODEL.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(facing.get2DDataValue() * 90));
        poseStack.translate(0.92, -0.1, -1.055);
        int[][] chessData = gomoku.getChessData();
        for (int[] row : chessData) {
            for (int j = 0; j < chessData[0].length; j++) {
                poseStack.translate(0, 0, 0.1316);
                if (row[j] == Point.BLACK) {
                    VertexConsumer blackPieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(BLACK_PIECE_TEXTURE));
                    PIECE_MODEL.renderToBuffer(poseStack, blackPieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                if (row[j] == Point.WHITE) {
                    VertexConsumer whitePieceBuff = bufferIn.getBuffer(RenderType.entityTranslucent(WHITE_PIECE_TEXTURE));
                    PIECE_MODEL.renderToBuffer(poseStack, whitePieceBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
            poseStack.translate(-0.1316, 0, -1.974);
        }
        poseStack.popPose();

        Camera camera = this.dispatcher.camera;
        if (!gomoku.isInProgress() && inRenderDistance(camera, gomoku.getBlockPos())) {
            MutableComponent loseTips;
            MutableComponent resetTips = Component.translatable("message.touhou_little_maid.gomoku.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
            if (gomoku.isPlayerTurn()) {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
            float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
            float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.mulPose(Axis.XN.rotationDegrees(camera.getXRot()));
            poseStack.scale(0.03F, -0.03F, 0.03F);
            this.font.drawInBatch(loseTips, loseTipsWidth, -10, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, Font.DisplayMode.POLYGON_OFFSET, 0, combinedLightIn);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.font.drawInBatch(resetTips, resetTipsWidth, 0, 0xFFFFFF, true, poseStack.last().pose(), bufferIn, Font.DisplayMode.POLYGON_OFFSET, 0, combinedLightIn);
            poseStack.popPose();
        }
    }

    private static boolean inRenderDistance(Camera camera, BlockPos pos) {
        return camera.getPosition().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 16 * 16;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityGomoku te) {
        return true;
    }
}
