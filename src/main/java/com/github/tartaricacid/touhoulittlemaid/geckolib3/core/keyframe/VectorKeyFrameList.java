/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * 矢量关键帧列表是一个方便的类，用于存储 3 个关键帧列表：X、Y 和 Z 关键帧。
 * 关键帧可以是旋转、缩放或位置。
 */
@SuppressWarnings("rawtypes")
public class VectorKeyFrameList<T extends KeyFrame> {
    public final List<T> xKeyFrames;
    public final List<T> yKeyFrames;
    public final List<T> zKeyFrames;

    public VectorKeyFrameList(List<T> XKeyFrames, List<T> YKeyFrames, List<T> ZKeyFrames) {
        this.xKeyFrames = XKeyFrames;
        this.yKeyFrames = YKeyFrames;
        this.zKeyFrames = ZKeyFrames;
    }

    public VectorKeyFrameList() {
        this.xKeyFrames = new ObjectArrayList<>();
        this.yKeyFrames = new ObjectArrayList<>();
        this.zKeyFrames = new ObjectArrayList<>();
    }

    public double getLastKeyframeTime() {
        double xTime = 0;
        for (T frame : this.xKeyFrames) {
            xTime += frame.getLength();
        }
        double yTime = 0;
        for (T frame : this.yKeyFrames) {
            yTime += frame.getLength();
        }
        double zTime = 0;
        for (T frame : this.zKeyFrames) {
            zTime += frame.getLength();
        }
        return Math.max(xTime, Math.max(yTime, zTime));
    }
}
