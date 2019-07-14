package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class ModelItem {
    @SerializedName("texture")
    private String texture;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("model")
    private String model;

    /**
     * 材质必须存在
     */
    public String getTexture() {
        return texture;
    }

    /**
     * 模型名称必须存在
     */
    public String getName() {
        return name;
    }

    @Nullable
    public List<String> getDescription() {
        return description;
    }

    /**
     * 模型必须存在
     */
    public String getModel() {
        return model;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}