/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneTopLevelSnapshot;

import javax.annotation.Nullable;

public class BoneAnimationQueue {
    public final BoneTopLevelSnapshot topLevelSnapshot;
    public final BoneSnapshot controllerSnapshot;
    @Nullable
    public BoneAnimation animation;

    // TODO: 待改进
    public AnimationPointQueue rotationXQueue = new AnimationPointQueue();
    public AnimationPointQueue rotationYQueue = new AnimationPointQueue();
    public AnimationPointQueue rotationZQueue = new AnimationPointQueue();
    public AnimationPointQueue positionXQueue = new AnimationPointQueue();
    public AnimationPointQueue positionYQueue = new AnimationPointQueue();
    public AnimationPointQueue positionZQueue = new AnimationPointQueue();
    public AnimationPointQueue scaleXQueue = new AnimationPointQueue();
    public AnimationPointQueue scaleYQueue = new AnimationPointQueue();
    public AnimationPointQueue scaleZQueue = new AnimationPointQueue();

    public BoneAnimationQueue(BoneTopLevelSnapshot snapshot) {
        this.topLevelSnapshot = snapshot;
        this.controllerSnapshot = new BoneSnapshot(snapshot);
    }

    public BoneSnapshot snapshot() {
        return controllerSnapshot;
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

    public void updateSnapshot() {
        this.controllerSnapshot.copyFrom(this.topLevelSnapshot);
    }

    // 链表重开比 clear() 快
    public void resetQueues() {
        this.rotationXQueue = new AnimationPointQueue();
        this.rotationYQueue = new AnimationPointQueue();
        this.rotationZQueue = new AnimationPointQueue();
        this.positionXQueue = new AnimationPointQueue();
        this.positionYQueue = new AnimationPointQueue();
        this.positionZQueue = new AnimationPointQueue();
        this.scaleXQueue = new AnimationPointQueue();
        this.scaleYQueue = new AnimationPointQueue();
        this.scaleZQueue = new AnimationPointQueue();
    }
}