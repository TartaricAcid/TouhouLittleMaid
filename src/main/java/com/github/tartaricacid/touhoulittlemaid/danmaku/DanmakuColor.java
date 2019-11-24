package com.github.tartaricacid.touhoulittlemaid.danmaku;

public enum DanmakuColor {
    // 各种颜色
    RED, ORANGE, YELLOW, LIME, LIGHT_GREEN, GREEN,
    CYAN, LIGHT_BLUE, BLUE, PURPLE, MAGENTA, PINK,
    WHITE;

    public static DanmakuColor getColor(int index) {
        if (index < 0 || index >= values().length) {
            return RED;
        }
        return values()[index];
    }

    public static int getLength() {
        return values().length;
    }
}
