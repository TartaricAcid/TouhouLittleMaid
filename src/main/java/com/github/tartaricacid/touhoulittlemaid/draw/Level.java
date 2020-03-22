package com.github.tartaricacid.touhoulittlemaid.draw;

import java.util.Locale;

public enum Level {
    // 扭蛋等级
    N, R, SR, SSR, UR;

    public String getName() {
        return this.name().toLowerCase(Locale.US);
    }
}
