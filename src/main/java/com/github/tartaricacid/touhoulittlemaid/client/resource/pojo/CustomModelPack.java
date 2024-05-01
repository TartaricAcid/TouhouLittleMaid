package com.github.tartaricacid.touhoulittlemaid.client.resource.pojo;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.util.Md5Utils;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
        // 多材质模型拆分
        List<T> newModelList = Lists.newArrayList();
        for (T item : modelList) {
            ResourceLocation modelId = item.getModelId();
            newModelList.add(item.extra(modelId, item.getTexture()));
            List<ResourceLocation> extraTextures = item.getExtraTextures();
            if (extraTextures != null && !extraTextures.isEmpty()) {
                extraTextures.forEach(r -> {
                    String suffix = Md5Utils.md5Hex(r.getPath()).toLowerCase(Locale.US);
                    ResourceLocation newModelId = new ResourceLocation(modelId.getNamespace(), modelId.getPath() + "_" + suffix);
                    newModelList.add(item.extra(newModelId, r));
                });
            }
        }
        modelList = newModelList;

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
