package com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo;

import com.google.gson.annotations.SerializedName;

public class Description {
    @SerializedName("texture_height")
    private int textureHeight;

    @SerializedName("texture_width")
    private int textureWidth;

    @SerializedName("visible_bounds_height")
    private float visibleBoundsHeight;

    @SerializedName("visible_bounds_width")
    private float visibleBoundsWidth;

    @SerializedName("visible_bounds_offset")
    private float[] visibleBoundsOffset;

    public int getTextureHeight() {
        return textureHeight;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public float getVisibleBoundsHeight() {
        return visibleBoundsHeight;
    }

    public float getVisibleBoundsWidth() {
        return visibleBoundsWidth;
    }

    public float[] getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }
}
