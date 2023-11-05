package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelProperties implements Serializable {
    @SerializedName("identifier")
    private String identifier;
    @SerializedName("texture_height")
    private double textureHeight;
    @SerializedName("texture_width")
    private double textureWidth;
    @SerializedName("visible_bounds_height")
    private double visibleBoundsHeight;
    @SerializedName("visible_bounds_width")
    private double visibleBoundsWidth;
    @SerializedName("visible_bounds_offset")
    private double[] visibleBoundsOffset;
    @SerializedName("ysm_height_scale")
    private double heightScale = 0.7;
    @SerializedName("ysm_width_scale")
    private double widthScale = 0.7;
    @SerializedName("ysm_extra_info")
    private ExtraInfo extraInfo = null;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getTextureHeight() {
        return textureHeight;
    }

    public void setTextureHeight(Double value) {
        this.textureHeight = value;
    }

    public Double getTextureWidth() {
        return textureWidth;
    }

    public void setTextureWidth(Double value) {
        this.textureWidth = value;
    }

    public Double getVisibleBoundsHeight() {
        return visibleBoundsHeight;
    }

    public void setVisibleBoundsHeight(Double value) {
        this.visibleBoundsHeight = value;
    }

    public Double getVisibleBoundsWidth() {
        return visibleBoundsWidth;
    }

    public void setVisibleBoundsWidth(Double value) {
        this.visibleBoundsWidth = value;
    }

    public double[] getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }

    public void setVisibleBoundsOffset(double[] value) {
        this.visibleBoundsOffset = value;
    }

    public double getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(double heightScale) {
        this.heightScale = heightScale;
    }

    public double getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(double widthScale) {
        this.widthScale = widthScale;
    }

    public ExtraInfo getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(ExtraInfo extraInfo) {
        this.extraInfo = extraInfo;
    }
}
