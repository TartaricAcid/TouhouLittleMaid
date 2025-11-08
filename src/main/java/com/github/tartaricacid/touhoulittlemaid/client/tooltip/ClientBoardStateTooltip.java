package com.github.tartaricacid.touhoulittlemaid.client.tooltip;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.GomokuCodec;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.BoardStateTooltip;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

import static com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.BoardStateTooltip.*;

public class ClientBoardStateTooltip implements ClientTooltipComponent {
    private static final ResourceLocation GOMOKU_BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/gomoku.png");
    private static final ResourceLocation XIANGQI_BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/xiangqi.png");
    private static final ResourceLocation CHESS_BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/chess.png");

    private static final BiFunction<String, String, Object> CACHE = Util.memoize(ClientBoardStateTooltip::getBoardGameData);

    private final String type;
    private final Object boardGameData;

    public ClientBoardStateTooltip(BoardStateTooltip tooltip) {
        this.boardGameData = CACHE.apply(tooltip.type(), tooltip.stateData());
        this.type = tooltip.type();
    }

    @Nullable
    private static Cloneable getBoardGameData(String type, String stateData) {
        switch (type) {
            case GOMOKU -> {
                return GomokuCodec.decode(stateData).board();
            }
            case XIANGQI -> {
                // java 笑笑传之，没有别名功能
                com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position position = new com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position();
                position.fromFen(stateData);
                return position.squares;
            }
            case CHESS -> {
                // java 笑笑传之，没有别名功能
                com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position position = new com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position();
                position.fromFen(stateData);
                return position.squares;
            }
        }
        return null;
    }

    @Override
    public int getHeight() {
        if (boardGameData == null) {
            return 0;
        }
        return switch (this.type) {
            case GOMOKU -> 80;
            case XIANGQI -> 132;
            case CHESS -> 108;
            default -> 0;
        };
    }

    @Override
    public int getWidth(Font font) {
        if (boardGameData == null) {
            return 0;
        }
        return switch (this.type) {
            case GOMOKU -> 76;
            case XIANGQI -> 128;
            case CHESS -> 102;
            default -> 0;
        };
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics graphics) {
        if (boardGameData == null) {
            return;
        }

        if (this.type.equals(GOMOKU) && boardGameData instanceof byte[][] data) {
            this.renderGomoku(pX, pY, graphics, data);
            return;
        }

        if (this.type.equals(XIANGQI) && boardGameData instanceof byte[] data) {
            this.renderXiangqi(pX, pY, graphics, data);
            return;
        }

        if (this.type.equals(CHESS) && boardGameData instanceof byte[] chessData) {
            this.renderChess(pX, pY, graphics, chessData);
        }
    }

    private void renderGomoku(int pX, int pY, GuiGraphics graphics, byte[][] data) {
        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 1);
        graphics.pose().translate(pX, pY, 0);
        graphics.blit(GOMOKU_BG, pX, pY, 0, 0, 151, 151);

        for (int y = 0; y <= 14; y++) {
            for (int x = 0; x <= 14; x++) {
                int piecesIndex = data[x][y];

                int v;
                if (piecesIndex == Point.BLACK) {
                    v = 151;
                } else if (piecesIndex == Point.WHITE) {
                    v = 160;
                } else {
                    continue;
                }

                int offsetX = pX + 1 + x * 10;
                int offsetY = pY + 1 + y * 10;

                graphics.blit(GOMOKU_BG, offsetX, offsetY, 0, v, 9, 9);
            }
        }

        graphics.pose().popPose();
    }

    private void renderXiangqi(int pX, int pY, GuiGraphics graphics, byte[] data) {
        graphics.blit(XIANGQI_BG, pX, pY, 0, 0, 128, 126);

        for (int y = 3; y <= 12; y++) {
            for (int x = 3; x <= 11; x++) {
                byte piecesIndex = CChessUtil.piecesIndex(x, y, data);

                int v;
                int u;
                if (CChessUtil.isRed(piecesIndex)) {
                    v = 126;
                    u = (piecesIndex - 8) * 11;
                } else if (CChessUtil.isBlack(piecesIndex)) {
                    v = 137;
                    u = (piecesIndex - 16) * 11;
                } else {
                    continue;
                }

                int offsetX = pX - 1 + (x - 3) * 13;
                int offsetY = pY - 1 + (y - 3) * 13;

                graphics.blit(XIANGQI_BG, offsetX, offsetY, u, v, 11, 11);
            }
        }
    }

    private void renderChess(int pX, int pY, GuiGraphics graphics, byte[] data) {
        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 1);
        graphics.pose().translate(pX, pY, 0);
        graphics.blit(CHESS_BG, pX, pY, 0, 0, 204, 204);

        for (int y = 0; y <= 7; y++) {
            for (int x = 4; x <= 11; x++) {
                byte piecesIndex = WChessUtil.piecesIndex(x, y, data);

                int v;
                int u;
                if (WChessUtil.isWhite(piecesIndex)) {
                    v = 204;
                    u = (piecesIndex - 8) * 24;
                } else if (WChessUtil.isBlack(piecesIndex)) {
                    v = 228;
                    u = (piecesIndex - 16) * 24;
                } else {
                    continue;
                }

                int offsetX = pX + 6 + (x - 4) * 24;
                int offsetY = pY + 6 + y * 24;

                graphics.blit(CHESS_BG, offsetX, offsetY, u, v, 24, 24);
            }
        }

        graphics.pose().popPose();
    }
}
