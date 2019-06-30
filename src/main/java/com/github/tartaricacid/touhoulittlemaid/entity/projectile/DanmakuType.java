package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

public enum DanmakuType {
    PELLET("pellet", 0, 0.8d), // 点弹
    BALL("ball", 1, 0.8d), // 小玉
    BIG_BALL("big_ball", 2, 0.8d), // 中玉
    BUBBLE("bubble", 3, 1.2d), // 大玉
    JELLYBEAN("jellybean", 4, 0.4d), // 椭弹
    HEART("heart", 5, 0.4d), // 心弹
    STAR("star", 6, 0.3d), // 星弹
    AMULET("amulet", 7, 0.3d), // 札弹
    ORBS("orbs", 8, 0.3d); // 环玉

    private String name;
    private int index;
    private double size;

    /**
     * 弹幕类型枚举
     *
     * @param name  弹幕名称
     * @param index 弹幕数字索引
     * @param size  弹幕大小
     */
    DanmakuType(String name, int index, double size) {
        this.name = name;
        this.index = index;
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
        return DanmakuType.values().length;
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
