/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

import java.util.List;

@SuppressWarnings("rawtypes")
public class AnimationData {
    private final List<AnimationController> animationControllers = new ReferenceArrayList<>(32);
    public double tick;
    public boolean isFirstTick = true;
    public double startTick = -1;
    public boolean shouldPlayWhilePaused = false;
    private double resetTickLength = 1;

    public AnimationData() {
    }

    public AnimationController addAnimationController(AnimationController value) {
        animationControllers.add(value);
        return value;
    }

    public double getResetSpeed() {
        return resetTickLength;
    }

    /**
     * 这是任何没有动画的骨骼恢复到其初始位置所需的时间
     *
     * @param resetTickLength 重置时所需的 tick。不能为负数
     */
    public void setResetSpeedInTicks(double resetTickLength) {
        this.resetTickLength = resetTickLength < 0 ? 0 : resetTickLength;
    }

    public List<AnimationController> getAnimationControllers() {
        return animationControllers;
    }
}
