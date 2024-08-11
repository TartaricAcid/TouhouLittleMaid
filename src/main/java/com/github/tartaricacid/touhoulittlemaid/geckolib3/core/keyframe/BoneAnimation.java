/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;

public class BoneAnimation {
    public final String boneName;
    public final VectorKeyFrameList<KeyFrame<IValue>> rotationKeyFrames;
    public final VectorKeyFrameList<KeyFrame<IValue>> positionKeyFrames;
    public final VectorKeyFrameList<KeyFrame<IValue>> scaleKeyFrames;

    public BoneAnimation(String boneName, VectorKeyFrameList<KeyFrame<IValue>> rotationKeyFrames, VectorKeyFrameList<KeyFrame<IValue>> positionKeyFrames, VectorKeyFrameList<KeyFrame<IValue>> scaleKeyFrames) {
        this.boneName = boneName;
        this.rotationKeyFrames = rotationKeyFrames;
        this.positionKeyFrames = positionKeyFrames;
        this.scaleKeyFrames = scaleKeyFrames;
    }
}