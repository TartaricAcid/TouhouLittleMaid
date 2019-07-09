package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CubesItem {
    @SerializedName("uv")
    private List<Integer> uv;

    @SerializedName("mirror")
    private boolean mirror;

    @SerializedName("size")
    private List<Integer> size;

    @SerializedName("origin")
    private List<Float> origin;

    public List<Integer> getUv() {
        return uv;
    }

    public boolean isMirror() {
        return mirror;
    }

    public List<Integer> getSize() {
        return size;
    }

    public List<Float> getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return
                "CubesItem{" +
                        "uv = '" + uv + '\'' +
                        ",mirror = '" + mirror + '\'' +
                        ",size = '" + size + '\'' +
                        ",origin = '" + origin + '\'' +
                        "}";
    }
}