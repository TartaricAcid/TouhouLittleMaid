package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CustomModelPackPOJO {
    private String date;

    @SerializedName("model_list")
    private List<ModelItem> modelList;

    @SerializedName("pack_name")
    private String packName;

    private List<String> author;

    private List<String> description;

    private String version;

    private int format;

    @Nullable
    public String getDate() {
        return date;
    }

    /**
     * 模型列表字段不为空
     */
    public List<ModelItem> getModelList() {
        return modelList;
    }

    /**
     * 包名不能为空
     */
    public String getPackName() {
        return packName;
    }

    public List<String> getAuthor() {
        return author;
    }

    public List<String> getDescription() {
        return description;
    }

    @Nullable
    public String getVersion() {
        return version;
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
     * 模型包 pojo 的二次修饰
     */
    public CustomModelPackPOJO decorate() {
        // 包名和 model list 不能为空
        if (packName == null) {
            throw new JsonSyntaxException("Expected \"pack_name\" in pack");
        }
        if (modelList == null || modelList.isEmpty()) {
            throw new JsonSyntaxException("Expected \"model_list\" in pack");
        }
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        if (author == null) {
            author = Collections.EMPTY_LIST;
        }

        // 为此包的模型对象进行二次修饰
        modelList.forEach(m -> m.decorate(format));
        return this;
    }
}