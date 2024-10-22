package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import net.minecraft.world.phys.Vec3;

public final class CChessUtil {
    // 女仆必输残局，测试用
    // rnbakab1r/9/8R/p1p1C4/1C4p1p/9/P1P1P1P1P/2N5N/9/R1BAKAB2
    // 长打残局
    // 1C1a2br1/3rak3/7c1/p3P2Rp/5n3/9/P1R3p1P/4C4/4A3N/2BAK3c
    // 六十回合自然限着
    // 3aka3/9/9/9/9/9/9/9/9/3AKA3
    public static final String INIT = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";

    public static boolean isClickResetArea(Vec3 clickPos) {
        double x = clickPos.x;
        double z = clickPos.z;
        if (1.199 < x && x < 1.431) {
            return (0.464 < z && z < 1.061) || (-1.061 < z && z < -0.464);
        }
        return false;
    }

    public static int getClickPosition(Vec3 clickPos) {
        double x = (clickPos.x + 1.365) / 0.304;
        double z = (clickPos.z + 1.370) / 0.304;
        int xRound = (int) Math.round(x);
        int zRound = (int) Math.round(z);
        double xAbs = Math.abs(xRound - x);
        double zAbs = Math.abs(zRound - z);

        if (xAbs < 0.3 && zAbs < 0.3) {
            xRound += Position.FILE_LEFT;
            zRound += Position.RANK_TOP;
            if (xRound <= Position.FILE_RIGHT && zRound <= Position.RANK_BOTTOM) {
                return Position.COORD_XY(xRound, zRound);
            }
        }
        return -1;
    }

    public static boolean isRed(byte piecesIndex) {
        return (piecesIndex & 8) == 8;
    }

    public static boolean isBlack(byte piecesIndex) {
        return (piecesIndex & 16) == 16;
    }

    public static boolean isPlayer(Position position) {
        return position.sdPlayer == 0;
    }

    public static boolean isMaid(Position position) {
        return position.sdPlayer == 1;
    }

    // 六十回自然限着
    public static boolean reachMoveLimit(Position position) {
        return position.moveNum > 60;
    }

    // 三回合长打
    public static boolean isRepeat(Position position) {
        return position.repStatus(3) > 0;
    }
}
