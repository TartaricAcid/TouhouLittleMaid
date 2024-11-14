package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import net.minecraft.world.phys.Vec3;

public final class WChessUtil {
    public static final String INIT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static boolean isClickResetArea(Vec3 clickPos) {
        double x = clickPos.x;
        double z = clickPos.z;
        if (-0.25 < z && z < 0.25) {
            return (1.125 < x && x < 1.375) || (-1.375 < x && x < -1.125);
        }
        return false;
    }

    public static int getClickPosition(Vec3 clickPos) {
        double x = (clickPos.x + 1) / 0.25;
        double z = (clickPos.z + 1) / 0.25;
        if (x < 0 || z < 0) {
            return -1;
        }
        int xFloor = (int) Math.floor(x);
        int zFloor = (int) Math.floor(z);

        xFloor += Position.FILE_LEFT;
        zFloor += Position.RANK_TOP;
        if (xFloor <= Position.FILE_RIGHT && zFloor <= Position.RANK_BOTTOM) {
            return Position.COORD_XY(xFloor, zFloor);
        }
        return -1;
    }

    public static boolean isWhite(byte piecesIndex) {
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

    // 五十回限制
    public static boolean reachMoveLimit(Position position) {
        return position.moveNum > (50 * 2);
    }

    // 三回合长打
    public static boolean isRepeat(Position position) {
        return position.isRep(2);
    }
}
