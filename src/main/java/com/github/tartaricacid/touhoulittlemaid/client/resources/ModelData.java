package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;

import javax.annotation.Nullable;
import java.util.List;

public class ModelData {
    private EntityModelJson model;
    private MaidModelInfo info;
    private List<Object> animations;

    public ModelData(EntityModelJson model, MaidModelInfo info, @Nullable List<Object> animations) {
        this.model = model;
        this.info = info;
        this.animations = animations;
    }

    public void setModel(EntityModelJson model) {
        this.model = model;
    }

    public void setInfo(MaidModelInfo info) {
        this.info = info;
    }

    public void setAnimations(List<Object> animations) {
        this.animations = animations;
    }

    public EntityModelJson getModel() {
        return model;
    }

    public MaidModelInfo getInfo() {
        return info;
    }

    @Nullable
    public List<Object> getAnimations() {
        return animations;
    }
}
