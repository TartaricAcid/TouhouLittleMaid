package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing;

import java.util.Locale;

public enum EasingType {
    // 插值类型
    NONE, CUSTOM, LINEAR, STEP;

    public static EasingType getEasingTypeFromString(String search) {
        return switch (search.toLowerCase(Locale.ROOT)) {
            default -> NONE;
            case "custom" -> CUSTOM;
            case "linear" -> LINEAR;
            case "step" -> STEP;
        };
    }
}