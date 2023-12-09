package com.github.tartaricacid.touhoulittlemaid.entity.favorability;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public final class FavorabilityManager {
    public static final int LEVEL_1 = 64;
    public static final int LEVEL_2 = 192;
    public static final int LEVEL_3 = 384;

    public static int getLevel(int favorability) {
        if (favorability < LEVEL_1) {
            return 0;
        } else if (favorability < LEVEL_2) {
            return 1;
        } else if (favorability < LEVEL_3) {
            return 2;
        } else {
            return 3;
        }
    }

    public static double getLevelPercent(int favorability) {
        if (favorability < LEVEL_1) {
            return favorability / (double) LEVEL_1;
        } else if (favorability < LEVEL_2) {
            return (favorability - LEVEL_1) / (double) (LEVEL_2 - LEVEL_1);
        } else if (favorability < LEVEL_3) {
            return (favorability - LEVEL_2) / (double) (LEVEL_3 - LEVEL_2);
        } else {
            return 0;
        }
    }

    public static void add(EntityMaid maid, int addPoint) {
        int favorability = maid.getFavorability();
        if (getLevel(favorability) < 3) {
            int result = favorability + addPoint;
            if (getLevel(favorability) == 3) {
                result = LEVEL_3;
            }
            maid.setFavorability(result);
        }
    }

    public static void reduce(EntityMaid maid, int reducePoint) {
        int favorability = maid.getFavorability();
        int result = favorability - reducePoint;
        if (favorability < 0) {
            result = 0;
        }
        maid.setFavorability(result);
    }

    public static void max(EntityMaid maid) {
        maid.setFavorability(LEVEL_3);
    }
}
