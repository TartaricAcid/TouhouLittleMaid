package com.github.tartaricacid.touhoulittlemaid.client.sound.pojo;

import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
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
    @SerializedName("icon_delay")
    private int iconDelay = 2;
    @Expose(deserialize = false)
    private CustomModelPack.AnimationState iconAnimation = CustomModelPack.AnimationState.UNCHECK;
    @Expose(deserialize = false)
    private int iconAspectRatio = 1;

    @SerializedName("url")
    private String url;

    @Nullable
    public String getDate() {
        return date;
    }

    public String getPackName() {
        return packName;
    }

    public List<String> getAuthor() {
        return author;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    @Nullable
    public ResourceLocation getIcon() {
        return icon;
    }

    public CustomModelPack.AnimationState getIconAnimation() {
        return iconAnimation;
    }

    public void setIconAnimation(CustomModelPack.AnimationState iconAnimation) {
        this.iconAnimation = iconAnimation;
    }

    public int getIconAspectRatio() {
        return iconAspectRatio;
    }

    public void setIconAspectRatio(int iconAspectRatio) {
        this.iconAspectRatio = iconAspectRatio;
    }

    public int getIconDelay() {
        return iconDelay;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @SuppressWarnings("unchecked")
    public SoundPackInfo decorate() {
        // 包名不能为空
        if (packName == null) {
            throw new JsonSyntaxException("Expected \"pack_name\" in pack");
        }
        if (author == null) {
            author = Collections.EMPTY_LIST;
        }
        if (iconDelay <= 0) {
            iconDelay = 1;
        }
        return this;
    }
}
