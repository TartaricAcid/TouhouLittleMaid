package com.github.tartaricacid.touhoulittlemaid.util;

public enum VanillaColor {
    // 原版颜色枚举，能够获取到 RGB 值
    BLACK("BLACK", '0', 0, 0xFF000000),
    DARK_BLUE("DARK_BLUE", '1', 1, 0xFF00002A),
    DARK_GREEN("DARK_GREEN", '2', 2, 0xFF002A00),
    DARK_AQUA("DARK_AQUA", '3', 3, 0xFF002A2A),
    DARK_RED("DARK_RED", '4', 4, 0xFF2A0000),
    DARK_PURPLE("DARK_PURPLE", '5', 5, 0xFF2A002A),
    GOLD("GOLD", '6', 6, 0xFF2A2A00),
    GRAY("GRAY", '7', 7, 0xFF2A2A2A),
    DARK_GRAY("DARK_GRAY", '8', 8, 0xFF151515),
    BLUE("BLUE", '9', 9, 0xFF15153F),
    GREEN("GREEN", 'a', 10, 0xFF153F15),
    AQUA("AQUA", 'b', 11, 0xFF153F3F),
    RED("RED", 'c', 12, 0xFF3F1515),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 0xFF3F153F),
    YELLOW("YELLOW", 'e', 14, 0xFF3F3F15),
    WHITE("WHITE", 'f', 15, 0xFF3F3F3F);

    private String name;
    private char code;
    private int index;
    private int color;

    VanillaColor(String name, char code, int index, int color) {
        this.name = name;
        this.code = code;
        this.index = index;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public char getCode() {
        return code;
    }

    public int getIndex() {
        return index;
    }

    public int getColor() {
        return color;
    }
}
