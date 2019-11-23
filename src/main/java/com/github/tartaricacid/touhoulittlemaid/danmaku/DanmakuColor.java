package com.github.tartaricacid.touhoulittlemaid.danmaku;

public enum DanmakuColor {
    // 各种颜色
    RED("red", 0),
    ORANGE("orange", 1),
    YELLOW("yellow", 2),
    GREEN("green", 3),
    CYAN("cyan", 4),
    BLUE("blue", 5),
    PURPLE("purple", 6);

    private String name;
    private int index;

    /**
     * 弹幕颜色枚举类型
     *
     * @param name  颜色名称
     * @param index 颜色索引
     */
    DanmakuColor(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static DanmakuColor getColor(int index) {
        for (DanmakuColor color : DanmakuColor.values()) {
            if (index == color.getIndex()) {
                return color;
            }
        }
        return RED;
    }

    public static int getLength() {
        return DanmakuColor.values().length;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
