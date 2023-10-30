/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */
package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public class AnimationBuilder {
    private final List<RawAnimation> animationList = new ObjectArrayList<>();

    public AnimationBuilder addAnimation(String animationName, ILoopType loopType) {
        animationList.add(new RawAnimation(animationName, loopType));
        return this;
    }

    public AnimationBuilder addAnimation(String animationName) {
        animationList.add(new RawAnimation(animationName, null));
        return this;
    }

    public AnimationBuilder addRepeatingAnimation(String animationName, int timesToRepeat) {
        assert timesToRepeat > 0;
        for (int i = 0; i < timesToRepeat; i++) {
            addAnimation(animationName, EDefaultLoopTypes.PLAY_ONCE);
        }
        return this;
    }

    public AnimationBuilder playOnce(String animationName) {
        return this.addAnimation(animationName, EDefaultLoopTypes.PLAY_ONCE);
    }

    public AnimationBuilder loop(String animationName) {
        return this.addAnimation(animationName, EDefaultLoopTypes.LOOP);
    }

    public AnimationBuilder playAndHold(String animationName) {
        return this.addAnimation(animationName, EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    }

    public AnimationBuilder clearAnimations() {
        animationList.clear();
        return this;
    }

    public List<RawAnimation> getRawAnimationList() {
        return animationList;
    }
}
