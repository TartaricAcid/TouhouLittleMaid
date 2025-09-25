package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.AIService;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.ZhiZhangAIService;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public final class MaidGomokuAI {
    public static final AIService EASY = new ZhiZhangAIService(new AIService.AIConfig(1, 10, false, 0, 6));
    public static final AIService NORMAL = new ZhiZhangAIService(new AIService.AIConfig(4, 10, false, 0, 6));
    public static final AIService HARD = new ZhiZhangAIService(new AIService.AIConfig(6, 10, false, 1, 8));
    public static final AIService HELL = new ZhiZhangAIService(new AIService.AIConfig(8, 10, false, 1, 10));
    public static final String GOMOKU = "Gomoku";
    public static final int EASY_COUNT = 2;
    public static final int NORMAL_COUNT = 8;
    public static final int HARD_COUNT = 24;

    public static int getRank(EntityMaid maid) {
        int maidCount = maid.getGameRecordManager().getGomokuWinCount();
        if (maidCount <= EASY_COUNT) {
            return 1;
        } else if (maidCount <= NORMAL_COUNT) {
            return 2;
        } else if (maidCount <= HARD_COUNT) {
            return 3;
        } else {
            return 4;
        }
    }

    public static AIService getService(int winCount) {
        if (winCount <= EASY_COUNT) {
            return EASY;
        } else if (winCount <= NORMAL_COUNT) {
            return NORMAL;
        } else if (winCount <= HARD_COUNT) {
            return HARD;
        } else {
            return HELL;
        }
    }

    /**
     * 获取棋局状态
     */
    public static Statue getStatue(byte[][] chessData, Point point) {
        int numRows = chessData.length;
        int numCols = chessData[0].length;
        int x = point.x;
        int y = point.y;
        int type = point.type;

        // 横轴
        int k = 1;
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            if (preX < 0) {
                break;
            }
            if (chessData[preX][y] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            if (nextX > numRows - 1) {
                break;
            }
            if (chessData[nextX][y] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 纵轴
        k = 1;
        for (int i = 1; i < 5; i++) {
            int preY = y - i;
            if (preY < 0) {
                break;
            }
            if (chessData[x][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextY = y + i;
            if (nextY > numCols - 1) {
                break;
            }
            if (chessData[x][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 左对角线
        k = 1;
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            int preY = y - i;
            if (preX < 0 || preY < 0) {
                break;
            }
            if (chessData[preX][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            int nextY = y + i;
            if (nextX > numRows - 1 || nextY > numCols - 1) {
                break;
            }
            if (chessData[nextX][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 右对角线
        k = 1;
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            int preY = y - i;
            if (nextX > numRows - 1 || preY < 0) {
                break;
            }
            if (chessData[nextX][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            int nextY = y + i;
            if (preX < 0 || nextY > numCols - 1) {
                break;
            }
            if (chessData[preX][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 检查和局：棋盘已满且无胜局
        boolean full = true;
        for (int i = 0; full && i < numRows; i++) {
            for (int j = 0; full && j < numCols; j++) {
                if (chessData[i][j] == 0) {
                    full = false;
                }
            }
        }
        if (full) {
            return Statue.DRAW;
        }

        return Statue.IN_PROGRESS;
    }
}
