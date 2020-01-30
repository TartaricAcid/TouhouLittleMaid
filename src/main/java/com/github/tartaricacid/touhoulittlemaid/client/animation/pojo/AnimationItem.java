package com.github.tartaricacid.touhoulittlemaid.client.animation.pojo;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * @author TartaricAcid
 * @date 2020/1/29 18:44
 **/
public class AnimationItem {
    private boolean loop = false;

    @SerializedName("animation_length")
    private float animationLength = 0.0f;

    private HashMap<String, KeyFrameItem> bones = Maps.newHashMap();

    public boolean isLoop() {
        return loop;
    }

    public float getAnimationLength() {
        return animationLength;
    }

    public HashMap<String, KeyFrameItem> getBones() {
        return bones;
    }
}
