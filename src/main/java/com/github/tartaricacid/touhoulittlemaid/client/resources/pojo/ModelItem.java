package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelItem {
    private String name;

    private List<String> description;

    private ResourceLocation model;

    private ResourceLocation texture;

    @SerializedName("model_id")
    private ResourceLocation modelId;

    /**
     * 用来为以后可能做改动的而设置的参数
     */
    private int format = -1;

    /**
     * 材质必须存在
     */
    public ResourceLocation getTexture() {
        return texture;
    }

    /**
     * 模型名称必须存在
     */
    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    /**
     * model id 必须存在
     */
    public ResourceLocation getModelId() {
        return modelId;
    }

    /**
     * 模型必须存在
     */
    public ResourceLocation getModel() {
        return model;
    }

    public int getFormat() {
        return format;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * 二次修饰此 pojo
     *
     * @param format 该模型对象所属包的 format
     */
    public ModelItem decorate(int format) {
        // 如果该模型对象的 format 为默认值，才允许设置所属包的 format
        if (this.format < 0) {
            this.format = format;
        }
        // description 设置为空列表
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        if (modelId == null) {
            if (model != null) {
                Pattern pattern = Pattern.compile("^.+\\/(.+)\\.json$");
                Matcher matcher = pattern.matcher(model.getPath());
                if (matcher.find()) {
                    modelId = new ResourceLocation(model.getNamespace(), matcher.group(1));
                }
            }
        } else {
            model = new ResourceLocation(modelId.getNamespace(), "models/entity/" + modelId.getPath() + ".json");
            texture = new ResourceLocation(modelId.getNamespace(), "textures/entity/" + modelId.getPath() + ".png");
        }
        // 如果 model_id 为空，抛出异常
        if (modelId == null) {
            throw new JsonSyntaxException("Expected \"model_id\" in model");
        }
        // 如果 name 为空，抛出异常
        if (name == null) {
            throw new JsonSyntaxException("Expected \"name\" in model");
        }
        return this;
    }
}