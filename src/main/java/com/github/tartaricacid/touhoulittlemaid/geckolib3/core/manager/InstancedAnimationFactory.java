package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;


public class InstancedAnimationFactory extends AnimationFactory {
    private AnimationData animationData;

    public InstancedAnimationFactory(IAnimatable animatable) {
        super(animatable);
    }

    @Override
        public AnimationData getOrCreateAnimationData(int uniqueID) {
        if (this.animationData == null) {
            this.animationData = new AnimationData();
            this.animatable.registerControllers(this.animationData);
        }
        return this.animationData;
    }
}