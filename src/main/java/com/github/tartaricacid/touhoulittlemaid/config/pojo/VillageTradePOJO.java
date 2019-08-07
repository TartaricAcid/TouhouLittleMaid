package com.github.tartaricacid.touhoulittlemaid.config.pojo;

import com.google.gson.annotations.SerializedName;

public class VillageTradePOJO {
    @SerializedName("level")
    private int level;

    @SerializedName("in")
    private In in;

    @SerializedName("out")
    private Out out;

    public int getLevel() {
        return level;
    }

    public In getIn() {
        return in;
    }

    public Out getOut() {
        return out;
    }
}