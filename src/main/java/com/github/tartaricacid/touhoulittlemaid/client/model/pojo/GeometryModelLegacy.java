package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeometryModelLegacy {
    @SerializedName("bones")
    private List<BonesItem> bones;

    @SerializedName("textureheight")
    private int textureheight;

    @SerializedName("texturewidth")
    private int texturewidth;

    @SerializedName("visible_bounds_height")
    private float visibleBoundsHeight;

    @SerializedName("visible_bounds_width")
    private float visibleBoundsWidth;

    @SerializedName("visible_bounds_offset")
    private List<Float> visibleBoundsOffset;

    public List<BonesItem> getBones() {
        return bones;
    }

    public int getTextureheight() {
        return textureheight;
    }

    public int getTexturewidth() {
        return texturewidth;
    }

    public float getVisibleBoundsHeight() {
        return visibleBoundsHeight;
    }

    public float getVisibleBoundsWidth() {
        return visibleBoundsWidth;
    }

    public List<Float> getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }

    @Override
    public String toString() {
        return
                "GeometryModel{" +
                        "bones = '" + bones + '\'' +
                        ",textureheight = '" + textureheight + '\'' +
                        ",texturewidth = '" + texturewidth + '\'' +
                        ",visible_bounds_height = '" + visibleBoundsHeight + '\'' +
                        ",visible_bounds_width = '" + visibleBoundsWidth + '\'' +
                        ",visible_bounds_offset = '" + visibleBoundsOffset + '\'' +
                        "}";
    }
}