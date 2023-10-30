package com.github.tartaricacid.touhoulittlemaid.mclib.utils;

public class MathHelper {
    /**
     * 将角度减小到 -180 到 +180 之间的角度，并进行 360 度检查
     */
    public static float wrapDegrees(float value) {
        value = value % 360.0F;
        if (value >= 180.0F) {
            value -= 360.0F;
        }
        if (value < -180.0F) {
            value += 360.0F;
        }
        return value;
    }

    /**
     * 将角度减小到 -180 到 +180 之间的角度，并进行 360 度检查
     */
    public static double wrapDegrees(double value) {
        value = value % 360.0D;
        if (value >= 180.0D) {
            value -= 360.0D;
        }
        if (value < -180.0D) {
            value += 360.0D;
        }
        return value;
    }

    /**
     * 调整角度，使其值在 [-180, 180]
     */
    public static int wrapDegrees(int angle) {
        angle = angle % 360;
        if (angle >= 180) {
            angle -= 360;
        }
        if (angle < -180) {
            angle += 360;
        }
        return angle;
    }
}
