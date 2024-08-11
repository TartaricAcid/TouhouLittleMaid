/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */
package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.EventKeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.ParticleEventKeyFrame;

import java.util.List;

public class Animation {
    public final String animationName;
    public final double animationLength;
    public final ILoopType loop;
    public final List<BoneAnimation> boneAnimations;
    public final List<EventKeyFrame<String>> soundKeyFrames;
    public final List<ParticleEventKeyFrame> particleKeyFrames;
    public final List<EventKeyFrame<String>> customInstructionKeyframes;

    public Animation(String animationName, double animationLength, ILoopType loop, List<BoneAnimation> boneAnimations, List<EventKeyFrame<String>> soundKeyFrames, List<ParticleEventKeyFrame> particleKeyFrames, List<EventKeyFrame<String>> customInstructionKeyframes) {
        this.animationName = animationName;
        this.animationLength = animationLength;
        this.loop = loop;
        this.boneAnimations = boneAnimations;
        this.soundKeyFrames = soundKeyFrames;
        this.particleKeyFrames = particleKeyFrames;
        this.customInstructionKeyframes = customInstructionKeyframes;
    }
}
