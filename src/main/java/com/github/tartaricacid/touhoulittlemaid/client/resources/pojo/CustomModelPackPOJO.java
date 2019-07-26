package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

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

    public CustomModelPackPOJO decorate() {
        if (modelList == null) {
            modelList = Collections.EMPTY_LIST;
        }
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        if (author == null) {
            author = Collections.EMPTY_LIST;
        }

        modelList.forEach(m -> m.decorate(format));
        return this;
    }
}