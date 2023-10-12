package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

public class Cube {
    @SerializedName("inflate")
    private Double inflate;
    @SerializedName("mirror")
    private Boolean mirror;
    @SerializedName("origin")
    private double[] origin = new double[]{0, 0, 0};
    @SerializedName("pivot")
    private double[] pivot = new double[]{0, 0, 0};
    @SerializedName("rotation")
    private double[] rotation = new double[]{0, 0, 0};
    @SerializedName("size")
    private double[] size = new double[]{1, 1, 1};
    @SerializedName("uv")
    private UvUnion uv;

    public Double getInflate() {
        return inflate;
    }

    public void setInflate(Double value) {
        this.inflate = value;
    }

    public Boolean getMirror() {
        return mirror;
    }

    public void setMirror(Boolean value) {
        this.mirror = value;
    }

    public double[] getOrigin() {
        return origin;
    }

    public void setOrigin(double[] value) {
        this.origin = value;
    }

    public double[] getPivot() {
        return pivot;
    }

    public void setPivot(double[] value) {
        this.pivot = value;
    }

    public double[] getRotation() {
        return rotation;
    }

    public void setRotation(double[] value) {
        this.rotation = value;
    }

    public double[] getSize() {
        return size;
    }

    public void setSize(double[] value) {
        this.size = value;
    }

    public UvUnion getUv() {
        return uv;
    }

    public void setUv(UvUnion value) {
        this.uv = value;
    }
}
