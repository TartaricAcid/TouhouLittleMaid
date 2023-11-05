package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocatorClass implements Serializable {
    @SerializedName("ignore_inherited_scale")
    private Boolean ignoreInheritedScale;
    @SerializedName("offset")
    private double[] offset;
    @SerializedName("rotation")
    private double[] rotation;

    public Boolean getIgnoreInheritedScale() {
        return ignoreInheritedScale;
    }

    public void setIgnoreInheritedScale(Boolean value) {
        this.ignoreInheritedScale = value;
    }

    public double[] getOffset() {
        return offset;
    }

    public void setOffset(double[] value) {
        this.offset = value;
    }

    public double[] getRotation() {
        return rotation;
    }

    public void setRotation(double[] value) {
        this.rotation = value;
    }
}
