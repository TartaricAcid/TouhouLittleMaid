package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonSyntaxException;

import net.minecraft.util.ResourceLocation;

public class ModelItem {
    private String name;

    private List<String> description;

    private ResourceLocation model;

    private ResourceLocation texture;

    private ResourceLocation location;

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

    public ResourceLocation getLocation() {
        return location;
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

    public ModelItem decorate(int format) {
        if (this.format < 0) {
            this.format = format;
        }
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        if (location == null) {
            if (model != null) {
                Pattern pattern = Pattern.compile("^.+\\/(.+)\\.json$");
                Matcher matcher = pattern.matcher(model.getPath());
                if (matcher.find()) {
                    location = new ResourceLocation(model.getNamespace(), matcher.group(1));
                }
            }
            if (location == null) {
                throw new JsonSyntaxException("Expected \"location\" in model");
            }
        } else {
            model = new ResourceLocation(location.getNamespace(), "models/entity/" + location.getPath() + ".json");
            texture = new ResourceLocation(location.getNamespace(), "textures/entity/" + location.getPath() + ".png");
        }
        return this;
    }
}