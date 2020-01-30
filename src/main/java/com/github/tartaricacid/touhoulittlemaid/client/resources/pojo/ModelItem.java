package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ModelItem {
    private String name;

    private List<String> description;

    private ResourceLocation model;

    private ResourceLocation texture;

    @SerializedName("model_id")
    private ResourceLocation modelId;

    @SerializedName("render_item_scale")
    private float renderItemScale = 1.0f;

    private HashMap<String, ResourceLocation> animations = Maps.newHashMap();

    /**
     * 用来为以后可能做改动的而设置的参数
     */
    @Deprecated
    private int format = -1;
    @SerializedName("mounted_height")
    private float mountedYOffset;
    @SerializedName("tameable_can_ride")
    private boolean tameableCanRide = true;
    @SerializedName("no_gravity")
    private boolean noGravity = false;

    public ResourceLocation getTexture() {
        return texture;
    }

    /*--------------------------------- 坐垫部分数据 ------------------------------*/

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public HashMap<String, ResourceLocation> getAnimations() {
        return animations;
    }

    /*--------------------------------- 坐垫部分数据 ------------------------------*/

    /**
     * model id 必须存在
     */
    public ResourceLocation getModelId() {
        return modelId;
    }

    public ResourceLocation getModel() {
        return model;
    }

    public float getMountedYOffset() {
        return mountedYOffset;
    }

    public boolean isTameableCanRide() {
        return tameableCanRide;
    }

    public float getRenderItemScale() {
        return renderItemScale;
    }

    public boolean isNoGravity() {
        return noGravity;
    }

    @Deprecated
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
        // 将写入的高度转换为游戏内部的高度
        mountedYOffset = (mountedYOffset - 3) * 0.0625f;
        return this;
    }
}