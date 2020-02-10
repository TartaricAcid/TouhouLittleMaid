package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class MaidModelItem implements IModelItem {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("model")
    private ResourceLocation model;

    @SerializedName("texture")
    private ResourceLocation texture;

    @SerializedName("model_id")
    private ResourceLocation modelId;

    @SerializedName("render_item_scale")
    private float renderItemScale = 1.0f;

    private ResourceLocation animation;

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public ResourceLocation getAnimation() {
        return animation;
    }

    @Override
    public ResourceLocation getModelId() {
        return modelId;
    }

    @Override
    public ResourceLocation getModel() {
        return model;
    }

    @Override
    public float getRenderItemScale() {
        return renderItemScale;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MaidModelItem decorate() {
        // description 设置为空列表
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        // 如果 model_id 为空，抛出异常
        if (modelId == null) {
            throw new JsonSyntaxException("Expected \"model_id\" in model");
        }
        // 如果 model 或 texture 为空，自动生成默认位置的模型
        if (model == null) {
            model = new ResourceLocation(modelId.getNamespace(), "models/entity/" + modelId.getPath() + ".json");
        }
        if (texture == null) {
            texture = new ResourceLocation(modelId.getNamespace(), "textures/entity/" + modelId.getPath() + ".png");
        }
        // 如果名称为空，自动生成本地化名称
        if (name == null) {
            name = String.format("{model.%s.%s.name}", modelId.getNamespace(), modelId.getPath());
        }
        if (animation == null) {
            animation = new ResourceLocation("touhou_little_maid:animation/maid.default.js");
        }
        return this;
    }
}
