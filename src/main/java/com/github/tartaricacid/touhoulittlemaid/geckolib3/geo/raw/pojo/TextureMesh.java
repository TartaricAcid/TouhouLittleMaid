package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TextureMesh implements Serializable {
    @SerializedName("local_pivot")
    private double[] localPivot;
    @SerializedName("position")
    private double[] position;
    @SerializedName("rotation")
    private double[] rotation;
    @SerializedName("scale")
    private double[] scale;
    @SerializedName("texture")
    private String texture;

    public double[] getLocalPivot() {
        return localPivot;
    }

    public void setLocalPivot(double[] value) {
        this.localPivot = value;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] value) {
        this.position = value;
    }

    public double[] getRotation() {
        return rotation;
    }

    public void setRotation(double[] value) {
        this.rotation = value;
    }

    public double[] getScale() {
        return scale;
    }

    public void setScale(double[] value) {
        this.scale = value;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String value) {
        this.texture = value;
    }
}
