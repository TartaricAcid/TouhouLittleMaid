package com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo;

import com.google.gson.annotations.SerializedName;

public class FaceItem {
    @SerializedName("uv")
    private float[] uv;

    @SerializedName("uv_size")
    private float[] uvSize;

    public FaceItem(float[] uv, float[] uvSize) {
        this.uv = uv;
        this.uvSize = uvSize;
    }

    public float[] getUv() {
        return uv;
    }

    public float[] getUvSize() {
        return uvSize;
    }
}
