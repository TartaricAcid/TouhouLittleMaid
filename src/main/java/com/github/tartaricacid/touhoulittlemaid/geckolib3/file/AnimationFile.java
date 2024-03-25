package com.github.tartaricacid.touhoulittlemaid.geckolib3.file;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;

import java.util.Collection;
import java.util.Map;

public record AnimationFile(Map<String, Animation> animations) {
    public AnimationFile() {
        this(new Object2ReferenceOpenHashMap<>());
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Collection<Animation> getAllAnimations() {
        return this.animations.values();
    }

    public void putAnimation(String name, Animation animation) {
        this.animations.put(name, animation);
    }
}