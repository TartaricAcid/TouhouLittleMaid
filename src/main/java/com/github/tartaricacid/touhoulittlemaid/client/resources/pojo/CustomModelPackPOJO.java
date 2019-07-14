package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class CustomModelPackPOJO {
    @SerializedName("date")
    private String date;

    @SerializedName("model_list")
    private List<ModelItem> modelList;

    @SerializedName("pack_name")
    private String packName;

    @SerializedName("author")
    private List<String> author;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("version")
    private String version;

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

    @Nullable
    public List<String> getAuthor() {
        return author;
    }

    @Nullable
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
}