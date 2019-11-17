package com.github.tartaricacid.touhoulittlemaid.danmaku.pojo;

import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("author")
    private String author;

    @SerializedName("texture")
    private String texture;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("version")
    private String version;

    public String getAuthor() {
        return author;
    }

    public String getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }
}