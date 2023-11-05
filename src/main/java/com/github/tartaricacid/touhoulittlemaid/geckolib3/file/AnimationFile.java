package com.github.tartaricacid.touhoulittlemaid.geckolib3.file;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Collection;
import java.util.Map;

public class AnimationFile {
    private final Map<String, Animation> animations;

    public AnimationFile(Map<String, Animation> animations) {
        this.animations = animations;
    }

    public AnimationFile() {
        this(new Object2ObjectOpenHashMap<>());
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Collection<Animation> getAllAnimations() {
        return this.animations.values();
    }

    public Map<String, Animation> animations() {
        return animations;
    }

    public void putAnimation(String name, Animation animation) {
        this.animations.put(name, animation);
    }
}