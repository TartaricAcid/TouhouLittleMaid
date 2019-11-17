package com.github.tartaricacid.touhoulittlemaid.danmaku.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Danmaku {
    @SerializedName("times")
    private int times;

    @SerializedName("delay")
    private int delay;

    @SerializedName("color")
    private List<String> color;

    @SerializedName("function")
    private List<DanmakuFunction> danmakuFunction;

    @SerializedName("style")
    private List<String> style;

    public int getTimes() {
        return times;
    }

    public int getDelay() {
        return delay;
    }

    public List<String> getColor() {
        return color;
    }

    public List<DanmakuFunction> getDanmakuFunction() {
        return danmakuFunction;
    }

    public List<String> getStyle() {
        return style;
    }
}