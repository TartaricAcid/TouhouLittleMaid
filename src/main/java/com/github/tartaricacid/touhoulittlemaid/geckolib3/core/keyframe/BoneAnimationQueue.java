/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;

public class BoneAnimationQueue {
    public final IBone bone;
    public final AnimationPointQueue rotationXQueue = new AnimationPointQueue();
    public final AnimationPointQueue rotationYQueue = new AnimationPointQueue();
    public final AnimationPointQueue rotationZQueue = new AnimationPointQueue();
    public final AnimationPointQueue positionXQueue = new AnimationPointQueue();
    public final AnimationPointQueue positionYQueue = new AnimationPointQueue();
    public final AnimationPointQueue positionZQueue = new AnimationPointQueue();
    public final AnimationPointQueue scaleXQueue = new AnimationPointQueue();
    public final AnimationPointQueue scaleYQueue = new AnimationPointQueue();
    public final AnimationPointQueue scaleZQueue = new AnimationPointQueue();

    public BoneAnimationQueue(IBone bone) {
        this.bone = bone;
    }

    public IBone bone() {
        return bone;
    }

    public AnimationPointQueue rotationXQueue() {
        return rotationXQueue;
    }

    public AnimationPointQueue rotationYQueue() {
        return rotationYQueue;
    }

    public AnimationPointQueue rotationZQueue() {
        return rotationZQueue;
    }

    public AnimationPointQueue positionXQueue() {
        return positionXQueue;
    }

    public AnimationPointQueue positionYQueue() {
        return positionYQueue;
    }

    public AnimationPointQueue positionZQueue() {
        return positionZQueue;
    }

    public AnimationPointQueue scaleXQueue() {
        return scaleXQueue;
    }

    public AnimationPointQueue scaleYQueue() {
        return scaleYQueue;
    }

    public AnimationPointQueue scaleZQueue() {
        return scaleZQueue;
    }
}