package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeometryModelNew {
    @SerializedName("description")
    private Description description;

    @SerializedName("bones")
    private List<BonesItem> bones;

    public Description getDescription() {
        return description;
    }

    public List<BonesItem> getBones() {
        return bones;
    }
}
