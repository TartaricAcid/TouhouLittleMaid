package com.github.tartaricacid.touhoulittlemaid.danmaku.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DanmakuFunction {
    @SerializedName("color")
    private List<String> color;

    @SerializedName("x")
    private String X;

    @SerializedName("y")
    private String Y;

    @SerializedName("style")
    private List<String> style;

    @SerializedName("z")
    private String Z;

    public List<String> getColor() {
        return color;
    }

    public String getX() {
        return X;
    }

    public String getY() {
        return Y;
    }

    public List<String> getStyle() {
        return style;
    }

    public String getZ() {
        return Z;
    }
}