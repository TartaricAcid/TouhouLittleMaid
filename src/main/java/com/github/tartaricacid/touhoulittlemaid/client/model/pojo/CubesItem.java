package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class CubesItem {
    @SerializedName("uv")
    private List<Float> uv;

    @SerializedName("mirror")
    private boolean mirror;

    @SerializedName("inflate")
    private float inflate;

    @SerializedName("size")
    private List<Float> size;

    @SerializedName("origin")
    private List<Float> origin;

    @SerializedName("rotation")
    private List<Float> rotation;

    @SerializedName("pivot")
    private List<Float> pivot;

    public List<Float> getUv() {
        return uv;
    }

    public boolean isMirror() {
        return mirror;
    }

    public float getInflate() {
        return inflate;
    }

    /**
     * 基岩版这货居然可以为浮点数，服了
     */
    public List<Float> getSize() {
        return size;
    }

    public List<Float> getOrigin() {
        return origin;
    }

    @Nullable
    public List<Float> getRotation() {
        return rotation;
    }

    @Nullable
    public List<Float> getPivot() {
        return pivot;
    }

    @Override
    public String toString() {
        return
                "CubesItem{" +
                        "uv = '" + uv + '\'' +
                        ",inflate = '" + inflate + '\'' +
                        ",mirror = '" + mirror + '\'' +
                        ",size = '" + size + '\'' +
                        ",origin = '" + origin + '\'' +
                        "}";
    }
}