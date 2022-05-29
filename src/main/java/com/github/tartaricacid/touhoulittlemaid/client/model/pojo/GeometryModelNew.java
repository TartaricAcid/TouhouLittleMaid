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

    public GeometryModelNew deco() {
        if (bones != null) {
            this.bones.forEach(bonesItem -> {
                if (bonesItem.getCubes() != null) {
                    bonesItem.getCubes().forEach(cubesItem -> {
                        if (!cubesItem.isHasMirror()) {
                            cubesItem.setMirror(bonesItem.isMirror());
                        }
                    });
                }
            });
        }
        return this;
    }
}
