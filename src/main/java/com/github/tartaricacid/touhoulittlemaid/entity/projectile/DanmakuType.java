package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

public enum DanmakuType {
    // 点弹
    PELLET("pellet", 0.8d),
    // 小玉
    BALL("ball", 0.8d),
    // 中玉
    BIG_BALL("big_ball", 0.8d),
    // 大玉
    BUBBLE("bubble", 1.2d),
    // 椭弹
    JELLYBEAN("jellybean", 0.4d),
    // 心弹
    HEART("heart", 0.4d),
    // 星弹
    STAR("star", 0.3d),
    // 札弹
    AMULET("amulet", 0.3d),
    // 环玉
    ORBS("orbs", 0.3d);

    private String name;
    private int index;
    private double size;
    private static int length = values().length;

    /**
     * 弹幕类型枚举
     *
     * @param name  弹幕名称
     * @param index 弹幕数字索引
     * @param size  弹幕大小
     */
    DanmakuType(String name, double size) {
        this.name = name;
        this.index = ordinal();
        this.size = size;
    }

    public static DanmakuType getType(int index) {
        for (DanmakuType type : DanmakuType.values()) {
            if (index == type.getIndex()) {
                return type;
            }
        }
        return PELLET;
    }

    public static int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public double getSize() {
        return size;
    }
}
