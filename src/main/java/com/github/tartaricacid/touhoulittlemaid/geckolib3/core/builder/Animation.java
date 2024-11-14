/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */
package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.event.EventKeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

import java.util.List;

public class Animation {
    public final String animationName;
    public final double animationLength;
    public final ILoopType loop;
    public final List<BoneAnimation> boneAnimations;
    public final List<EventKeyFrame<IValue[]>> customInstructionKeyframes;

    public Animation(String animationName, double animationLength, ILoopType loop,
                     ReferenceArrayList<BoneAnimation> boneAnimations,
                     ReferenceArrayList<EventKeyFrame<IValue[]>> customInstructionKeyframes) {
        this.animationName = animationName;
        this.animationLength = animationLength;
        this.loop = loop;
        this.boneAnimations = boneAnimations;
        this.customInstructionKeyframes = customInstructionKeyframes;
    }
}