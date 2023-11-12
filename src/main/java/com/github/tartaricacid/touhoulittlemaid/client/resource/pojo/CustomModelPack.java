package com.github.tartaricacid.touhoulittlemaid.client.resource.pojo;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CustomModelPack<T extends IModelInfo> {
    @SerializedName("date")
    private String date;

    @SerializedName("model_list")
    private List<T> modelList;

    @SerializedName("pack_name")
    private String packName;

    @SerializedName("author")
    private List<String> author;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("version")
    private String version;

    @SerializedName("icon")
    private ResourceLocation icon;
    @SerializedName("icon_delay")
    private int iconDelay = 2;
    @Expose(deserialize = false)
    private AnimationState iconAnimation = AnimationState.UNCHECK;
    @Expose(deserialize = false)
    private int iconAspectRatio = 1;

    @Nullable
    public String getDate() {
        return date;
    }

    public List<T> getModelList() {
        return modelList;
    }

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

    @Nullable
    public ResourceLocation getIcon() {
        return icon;
    }

    public AnimationState getIconAnimation() {
        return iconAnimation;
    }

    public void setIconAnimation(AnimationState iconAnimation) {
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

    @SuppressWarnings("unchecked")
    public CustomModelPack<T> decorate() {
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
        if (icon == null) {
            icon = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/empty_model_pack_icon.png");
        }
        if (iconDelay <= 0) {
            iconDelay = 1;
        }

        // 为此包的模型对象进行二次修饰
        modelList.forEach(T::decorate);
        return this;
    }

    public enum AnimationState {
        // 拥有动画
        TRUE,
        // 没有动画
        FALSE,
        // 还未检查其是否拥有动画
        UNCHECK
    }
}
