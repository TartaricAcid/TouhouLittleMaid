package com.github.tartaricacid.touhoulittlemaid.danmaku;

public enum DanmakuColor {
    // 各种颜色
    RED(0xff0000),
    ORANGE(0xff7f00),
    YELLOW(0xfeff00),
    LIME(0x7fff00),
    LIGHT_GREEN(0x00ff00),
    GREEN(0x00ff7f),
    CYAN(0x1dfeff),
    LIGHT_BLUE(0x007fff),
    BLUE(0x0000ff),
    PURPLE(0x7f00ff),
    MAGENTA(0xff00fe),
    PINK(0xff007f),
    GRAY(0x8c8c8c);

    private int rgb;
    private int red;
    private int green;
    private int blue;
    private int floatRed;
    private int floatGreen;
    private int floatBlue;

    DanmakuColor(int color) {
        this.rgb = color;
        this.red = color >> 16 & 255;
        this.green = color >> 8 & 255;
        this.blue = color & 255;
        this.floatRed = this.red / 255;
        this.floatGreen = this.green / 255;
        this.floatBlue = this.blue / 255;
    }

    public static DanmakuColor getColor(int index) {
        if (index < 0 || index >= values().length) {
            return RED;
        }
        return values()[index];
    }

    public static int getLength() {
        return values().length;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getFloatRed() {
        return floatRed;
    }

    public int getFloatGreen() {
        return floatGreen;
    }

    public int getFloatBlue() {
        return floatBlue;
    }

    public int getRgb() {
        return rgb;
    }
}
