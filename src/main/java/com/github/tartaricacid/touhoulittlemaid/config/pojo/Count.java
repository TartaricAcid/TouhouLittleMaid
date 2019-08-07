package com.github.tartaricacid.touhoulittlemaid.config.pojo;

import com.google.gson.annotations.SerializedName;

public class Count {
    @SerializedName("min")
    private int min;

    @SerializedName("max")
    private int max;

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}