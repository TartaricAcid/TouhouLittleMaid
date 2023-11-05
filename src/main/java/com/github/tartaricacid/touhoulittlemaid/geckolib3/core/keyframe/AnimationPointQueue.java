/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;

import java.util.LinkedList;

/**
 * 动画点队列，这些动画点用于插值计算
 */
public class AnimationPointQueue extends LinkedList<AnimationPoint> {
    private static final long serialVersionUID = 5472797438476621193L;
    public IBone model;
}