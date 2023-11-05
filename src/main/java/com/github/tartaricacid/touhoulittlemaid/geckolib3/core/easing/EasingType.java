package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing;

import java.util.Locale;

public enum EasingType {
    // 插值类型
    NONE, CUSTOM, LINEAR, STEP;

    public static EasingType getEasingTypeFromString(String search) {
        switch (search.toLowerCase(Locale.ROOT)) {
            default:
                return NONE;
            case "custom":
                return CUSTOM;
            case "linear":
                return LINEAR;
            case "step":
                return STEP;
        }
    }
}