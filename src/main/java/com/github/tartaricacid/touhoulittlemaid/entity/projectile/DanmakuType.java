package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import java.util.Random;

public enum DanmakuType {
    // 点弹
    PELLET(0.6d),
    // 小玉
    BALL(0.5d),
    // 环玉
    ORBS(0.3d),
    // 中玉
    BIG_BALL(0.5d);

    private final double size;

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

    public static DanmakuType random(Random random) {
        return getType(random.nextInt(getLength()));
    }

    public static int getLength() {
        return values().length;
    }

    public double getSize() {
        return size;
    }
}
