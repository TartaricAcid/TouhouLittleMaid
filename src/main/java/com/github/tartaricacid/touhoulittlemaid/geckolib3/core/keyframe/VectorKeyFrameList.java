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
    public List<T> xKeyFrames;
    public List<T> yKeyFrames;
    public List<T> zKeyFrames;

    public VectorKeyFrameList(List<T> XKeyFrames, List<T> YKeyFrames, List<T> ZKeyFrames) {
        xKeyFrames = XKeyFrames;
        yKeyFrames = YKeyFrames;
        zKeyFrames = ZKeyFrames;
    }

    public VectorKeyFrameList() {
        xKeyFrames = new ObjectArrayList<>();
        yKeyFrames = new ObjectArrayList<>();
        zKeyFrames = new ObjectArrayList<>();
    }

    public double getLastKeyframeTime() {
        double xTime = 0;
        for (T frame : xKeyFrames) {
            xTime += frame.getLength();
        }
        double yTime = 0;
        for (T frame : yKeyFrames) {
            yTime += frame.getLength();
        }
        double zTime = 0;
        for (T frame : zKeyFrames) {
            zTime += frame.getLength();
        }
        return Math.max(xTime, Math.max(yTime, zTime));
    }
}
