package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

public class MolangUtils {
    public static final float TRUE = 1;
    public static final float FALSE = 0;

    public static float normalizeTime(long timestamp) {
        return ((float) (timestamp + 6000L) / 24000) % 1;
    }

    public static float booleanToFloat(boolean input) {
        return input ? TRUE : FALSE;
    }
}
