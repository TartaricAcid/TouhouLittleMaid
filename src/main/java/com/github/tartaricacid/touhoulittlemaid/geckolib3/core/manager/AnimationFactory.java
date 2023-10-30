package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class AnimationFactory {
    protected final IAnimatable animatable;
    private final Int2ObjectOpenHashMap<AnimationData> animationDataMap = new Int2ObjectOpenHashMap<>();

    @Deprecated
    public AnimationFactory(IAnimatable animatable) {
        this.animatable = animatable;
    }

    public AnimationData getOrCreateAnimationData(int uniqueID) {
        if (!this.animationDataMap.containsKey(uniqueID)) {
            AnimationData data = new AnimationData();
            this.animatable.registerControllers(data);
            this.animationDataMap.put(uniqueID, data);
        }
        return animationDataMap.get(uniqueID);
    }
}
