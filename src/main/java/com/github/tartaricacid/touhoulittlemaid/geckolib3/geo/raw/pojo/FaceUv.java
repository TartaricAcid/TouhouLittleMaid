package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FaceUv implements Serializable {
    @SerializedName("material_instance")
    private String materialInstance;
    @SerializedName("uv")
    private double[] uv;
    @SerializedName("uv_size")
    private double[] uvSize;

    public String getMaterialInstance() {
        return materialInstance;
    }

    public void setMaterialInstance(String value) {
        this.materialInstance = value;
    }

    public double[] getUv() {
        return uv;
    }

    public void setUv(double[] value) {
        this.uv = value;
    }

    public double[] getUvSize() {
        return uvSize;
    }

    public void setUvSize(double[] value) {
        this.uvSize = value;
    }
}
