package com.github.tartaricacid.touhoulittlemaid.danmaku;

public enum DanmakuType {
    // 点弹
    PELLET(0.8d),
    // 小玉
    BALL(0.8d),
    // 环玉
    ORBS(0.3d),
    // 中玉
    BIG_BALL(0.8d),
    // 大玉
    BUBBLE(1.2d),
    // 心弹
    HEART(0.4d),

    // TODO：完成下述弹幕渲染设计
    // 星弹
    STAR(0.3d),
    // 椭弹
    JELLYBEAN(0.4d),
    // 札弹
    AMULET(0.3d);

    private double size;

    /**
     * 弹幕类型枚举
     *
     * @param size 弹幕渲染放大缩小倍数
     */
    DanmakuType(double size) {
        this.size = size;
    }

    public static DanmakuType getType(int index) {
        if (index < 0 || index >= values().length) {
            return PELLET;
        }
        return values()[index];
    }

    public double getSize() {
        return size;
    }

    public static int getLength() {
        // TODO：目前未完成其他几种类型渲染设计
        return 6;
        // return values().length;
    }
}
