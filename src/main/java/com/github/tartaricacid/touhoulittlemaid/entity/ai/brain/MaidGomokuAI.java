package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.AIService;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.ZhiZhangAIService;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.Tag;

public final class MaidGomokuAI {
    public static final AIService EASY = new ZhiZhangAIService(new AIService.AIConfig(1, 10, false, 0, 6));
    public static final AIService NORMAL = new ZhiZhangAIService(new AIService.AIConfig(4, 10, false, 0, 6));
    public static final AIService HARD = new ZhiZhangAIService(new AIService.AIConfig(6, 10, false, 1, 8));
    public static final AIService HELL = new ZhiZhangAIService(new AIService.AIConfig(8, 10, false, 1, 10));
    public static final int EASY_COUNT = 2;
    public static final int NORMAL_COUNT = 8;
    public static final int HARD_COUNT = 24;

    public static int getRank(EntityMaid maid) {
        int maidCount = getMaidCount(maid);
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

    public static AIService getService(int maidCount) {
        if (maidCount <= EASY_COUNT) {
            return EASY;
        } else if (maidCount <= NORMAL_COUNT) {
            return NORMAL;
        } else if (maidCount <= HARD_COUNT) {
            return HARD;
        } else {
            return HELL;
        }
    }

    public static int getMaidCount(EntityMaid maid) {
        CompoundNBT gameSkill = maid.getGameSkill();
        if (gameSkill.contains("Gomoku", Tag.TAG_INT)) {
            return gameSkill.getInt("Gomoku");
        }
        return 0;
    }

    public static void addMaidCount(EntityMaid maid) {
        CompoundNBT gameSkill = maid.getGameSkill();
        if (gameSkill.contains("Gomoku", Tag.TAG_INT)) {
            gameSkill.putInt("Gomoku", gameSkill.getInt("Gomoku") + 1);
        } else {
            gameSkill.putInt("Gomoku", 1);
        }
        maid.setGameSkill(gameSkill);
    }

    /**
     * 获取棋局状态
     */
    public static Statue getStatue(int[][] chessData, Point point) {
        int rows = chessData[0].length;
        int cols = chessData.length;
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
            if (nextX > rows - 1) {
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
            if (nextY > cols - 1) {
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
            if (nextX > rows - 1 || nextY > cols - 1) {
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
            if (nextX > rows - 1 || preY < 0) {
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
            if (preX < 0 || nextY > cols - 1) {
                break;
            }
            if (chessData[preX][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        return Statue.IN_PROGRESS;
    }
}
