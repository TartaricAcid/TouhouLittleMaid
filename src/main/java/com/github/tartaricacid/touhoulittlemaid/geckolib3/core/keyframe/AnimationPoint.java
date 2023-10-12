/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;


@SuppressWarnings("rawtypes")
public class AnimationPoint {
    /**
     * 动画插值中要从中获取的当前 tick
     */
    public final double currentTick;
    /**
     * 当前动画结束的 tick
     */
    public final double animationEndTick;
    /**
     * 动画起始值
     */
    public final double animationStartValue;
    /**
     * 动画结束值
     */
    public final double animationEndValue;

    /**
     * 当前关键帧
     */
    public final KeyFrame keyframe;

    public AnimationPoint(KeyFrame keyframe, double tick, double animationEndTick, double animationStartValue, double animationEndValue) {
        this.keyframe = keyframe;
        this.currentTick = tick;
        this.animationEndTick = animationEndTick;
        this.animationStartValue = animationStartValue;
        this.animationEndValue = animationEndValue;
    }

    @Override

    public String toString() {
        return "Tick: " + currentTick + " | End Tick: " + animationEndTick + " | Start Value: " + animationStartValue
                + " | End Value: " + animationEndValue;
    }
}
