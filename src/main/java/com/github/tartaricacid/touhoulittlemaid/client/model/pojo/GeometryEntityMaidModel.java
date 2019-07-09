package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeometryEntityMaidModel {
    @SerializedName("bones")
    private List<BonesItem> bones;

    @SerializedName("textureheight")
    private int textureheight;

    @SerializedName("texturewidth")
    private int texturewidth;

    @SerializedName("visible_bounds_height")
    private int visibleBoundsHeight;

    @SerializedName("visible_bounds_width")
    private int visibleBoundsWidth;

    @SerializedName("visible_bounds_offset")
    private List<Integer> visibleBoundsOffset;

    public List<BonesItem> getBones() {
        return bones;
    }

    public int getTextureheight() {
        return textureheight;
    }

    public int getTexturewidth() {
        return texturewidth;
    }

    public int getVisibleBoundsHeight() {
        return visibleBoundsHeight;
    }

    public int getVisibleBoundsWidth() {
        return visibleBoundsWidth;
    }

    public List<Integer> getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }

    @Override
    public String toString() {
        return
                "GeometryEntityMaidModel{" +
                        "bones = '" + bones + '\'' +
                        ",textureheight = '" + textureheight + '\'' +
                        ",texturewidth = '" + texturewidth + '\'' +
                        ",visible_bounds_height = '" + visibleBoundsHeight + '\'' +
                        ",visible_bounds_width = '" + visibleBoundsWidth + '\'' +
                        ",visible_bounds_offset = '" + visibleBoundsOffset + '\'' +
                        "}";
    }
}