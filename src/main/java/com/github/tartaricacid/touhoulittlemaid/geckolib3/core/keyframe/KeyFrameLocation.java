/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

/**
 * 为动画存储一个时间，并返回需要执行的关键帧
 */
@SuppressWarnings("rawtypes")
public class KeyFrameLocation<T extends KeyFrame> {
    /**
     * 当前关键帧
     */
    public T currentFrame;
    /**
     * 这是之前所有关键帧的总时间
     */
    public double currentTick;

    public KeyFrameLocation(T currentFrame, double currentTick) {
        this.currentFrame = currentFrame;
        this.currentTick = currentTick;
    }
}
