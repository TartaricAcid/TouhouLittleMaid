/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone.BoneKeyFrame;

import java.util.List;

public class BoneAnimation {
    public final String boneName;
    public final List<BoneKeyFrame> rotationKeyFrames;
    public final List<BoneKeyFrame> positionKeyFrames;
    public final List<BoneKeyFrame> scaleKeyFrames;

    public BoneAnimation(String boneName, List<BoneKeyFrame> rotationKeyFrames, List<BoneKeyFrame> positionKeyFrames, List<BoneKeyFrame> scaleKeyFrames) {
        this.boneName = boneName;
        this.rotationKeyFrames = rotationKeyFrames;
        this.positionKeyFrames = positionKeyFrames;
        this.scaleKeyFrames = scaleKeyFrames;
    }
}