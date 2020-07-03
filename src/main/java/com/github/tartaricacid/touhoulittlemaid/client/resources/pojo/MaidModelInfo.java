package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

public class MaidModelInfo implements IModelInfo {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("model")
    private ResourceLocation model;

    @SerializedName("texture")
    private ResourceLocation texture;

    @SerializedName("model_id")
    private ResourceLocation modelId;

    @SerializedName("render_item_scale")
    private float renderItemScale = 1.0f;

    @SerializedName("render_entity_scale")
    private float renderEntityScale = 1.0f;

    @SerializedName("animation")
    private List<ResourceLocation> animation;

    @SerializedName("show_hata")
    private boolean showHata = true;

    @SerializedName("show_backpack")
    private boolean showBackpack = true;

    @SerializedName("show_custom_head")
    private boolean showCustomHead = true;

    @SerializedName("can_hold_trolley")
    private boolean canHoldTrolley = true;

    @SerializedName("can_hold_vehicle")
    private boolean canHoldVehicle = true;

    @SerializedName("can_riding_broom")
    private boolean canRidingBroom = true;

    @SerializedName("easter_egg")
    private EasterEgg easterEgg = null;

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public List<ResourceLocation> getAnimation() {
        return animation;
    }

    @Override
    public ResourceLocation getModelId() {
        return modelId;
    }

    @Override
    public ResourceLocation getModel() {
        return model;
    }

    @Override
    public float getRenderItemScale() {
        return renderItemScale;
    }

    public float getRenderEntityScale() {
        return renderEntityScale;
    }

    public boolean isShowHata() {
        return showHata;
    }

    public boolean isShowBackpack() {
        return showBackpack;
    }

    public boolean isShowCustomHead() {
        return showCustomHead;
    }

    public boolean isCanHoldTrolley() {
        return canHoldTrolley;
    }

    public boolean isCanHoldVehicle() {
        return canHoldVehicle;
    }

    public boolean isCanRidingBroom() {
        return canRidingBroom;
    }

    public EasterEgg getEasterEgg() {
        return easterEgg;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MaidModelInfo decorate() {
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
        if (animation == null || animation.size() == 0) {
            animation = Lists.newArrayList(
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/blink.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/beg.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/music_shake.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/leg/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/arm/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/arm/swing.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/arm/vertical.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/sit/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/armor/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/armor/reverse.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/wing/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/tail/default.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/sit/skirt_rotation.js"),
                    new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/base/float/default.js")
            );
        }
        renderEntityScale = MathHelper.clamp(renderEntityScale, 0.7f, 1.3f);
        return this;
    }
}
