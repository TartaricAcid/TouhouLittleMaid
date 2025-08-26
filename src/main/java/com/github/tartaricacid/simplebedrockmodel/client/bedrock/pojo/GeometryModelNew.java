package com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo;

import com.google.gson.annotations.SerializedName;

public class GeometryModelNew {
    @SerializedName("description")
    private Description description;

    @SerializedName("bones")
    private BonesItem[] bones;

    public Description getDescription() {
        return description;
    }

    public BonesItem[] getBones() {
        return bones;
    }

    public void deco() {
        if (bones == null) {
            return;
        }
        for (BonesItem bonesItem : this.bones) {
            if (bonesItem.getCubes() == null) {
                continue;
            }
            for (CubesItem cubesItem : bonesItem.getCubes()) {
                if (!cubesItem.isHasMirror()) {
                    cubesItem.setMirror(bonesItem.isMirror());
                }
            }
        }
    }
}
