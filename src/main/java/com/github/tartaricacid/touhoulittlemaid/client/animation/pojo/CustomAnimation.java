package com.github.tartaricacid.touhoulittlemaid.client.animation.pojo;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * @author TartaricAcid
 * @date 2020/1/29 18:43
 **/
public class CustomAnimation {
    @SerializedName("format_version")
    private String formatVersion = "";

    @SerializedName("animations")
    private HashMap<String, AnimationItem> animations = Maps.newHashMap();

    public String getFormatVersion() {
        return formatVersion;
    }

    public HashMap<String, AnimationItem> getAnimations() {
        return animations;
    }
}
