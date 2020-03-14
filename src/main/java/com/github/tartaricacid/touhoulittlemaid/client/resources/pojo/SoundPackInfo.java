package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class SoundPackInfo {
    @SerializedName("date")
    private String date;

    @SerializedName("pack_name")
    private String packName;

    @SerializedName("author")
    private List<String> author;

    @SerializedName("description")
    private String description;

    @SerializedName("version")
    private String version;

    @SerializedName("icon")
    private ResourceLocation icon;

    @SerializedName("url")
    private String url;

    public String getDate() {
        return date;
    }

    public String getPackName() {
        return packName;
    }

    public List<String> getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }

    @SuppressWarnings("unchecked")
    public SoundPackInfo decorate() {
        // 包名和 model list 不能为空
        if (packName == null) {
            throw new JsonSyntaxException("Expected \"pack_name\" in pack");
        }
        if (author == null) {
            author = Collections.EMPTY_LIST;
        }
        return this;
    }
}
