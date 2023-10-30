/*
    Direct copy of https://github.com/shedaniel/cloth-basic-math/blob/master/src/main/java/me/shedaniel/math/Color.java under the unlicense.
 */
package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util;



public final class Color {
    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color LIGHT_GRAY = new Color(0xFFC0C0C0);
    public static final Color GRAY = new Color(0xFF808080);
    public static final Color DARK_GRAY = new Color(0xFF404040);
    public static final Color BLACK = new Color(0xFF000000);
    public static final Color RED = new Color(0xFFFF0000);
    public static final Color PINK = new Color(0xFFFFAFAF);
    public static final Color ORANGE = new Color(0xFFFFC800);
    public static final Color YELLOW = new Color(0xFFFFFF00);
    public static final Color GREEN = new Color(0xFF00FF00);
    public static final Color MAGENTA = new Color(0xFFFF00FF);
    public static final Color CYAN = new Color(0xFF00FFFF);
    public static final Color BLUE = new Color(0xFF0000FF);
    private final int color;

    private Color(int color) {
        this.color = color;
    }

    public static Color ofTransparent(int color) {
        return new Color(color);
    }

    public static Color ofOpaque(int color) {
        return new Color(0xFF000000 | color);
    }

    public static Color ofRGB(float r, float g, float b) {
        return ofRGBA(r, g, b, 1f);
    }

    public static Color ofRGB(int r, int g, int b) {
        return ofRGBA(r, g, b, 255);
    }

    public static Color ofRGBA(float r, float g, float b, float a) {
        return ofRGBA((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
    }

    public static Color ofRGBA(int r, int g, int b, int a) {
        return new Color(((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    public static Color ofHSB(float hue, float saturation, float brightness) {
        return ofOpaque(HSBtoRGB(hue, saturation, brightness));
    }

    @SuppressWarnings("AlibabaSwitchStatement")
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    public int getColor() {
        return color;
    }

    public int getAlpha() {
        return color >> 24 & 0xFF;
    }

    public int getRed() {
        return color >> 16 & 0xFF;
    }

    public int getGreen() {
        return color >> 8 & 0xFF;
    }

    public int getBlue() {
        return color & 0xFF;
    }

    /**
     * 返回更加明亮的颜色
     *
     * @param factor 数值越高，颜色越亮
     * @return 更加明亮的颜色
     */
    public Color brighter(double factor) {
        int r = getRed(), g = getGreen(), b = getBlue();
        int i = (int) (1.0 / (1.0 - (1 / factor)));
        if (r == 0 && g == 0 && b == 0) {
            return ofRGBA(i, i, i, getAlpha());
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g > 0 && g < i) {
            g = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }
        return ofRGBA(Math.min((int) (r / (1 / factor)), 255), Math.min((int) (g / (1 / factor)), 255), Math.min((int) (b / (1 / factor)), 255), getAlpha());
    }

    /**
     * 返回更加深暗的颜色
     *
     * @param factor 数值越高，颜色越暗
     * @return 更加深暗的颜色
     */
    public Color darker(double factor) {
        return ofRGBA(Math.max((int) (getRed() * (1 / factor)), 0), Math.max((int) (getGreen() * (1 / factor)), 0), Math.max((int) (getBlue() * (1 / factor)), 0), getAlpha());
    }

    @Override
        public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return color == ((Color) other).color;
    }

    @Override
        public int hashCode() {
        return color;
    }

    @Override
        public String toString() {
        return String.valueOf(color);
    }
}
