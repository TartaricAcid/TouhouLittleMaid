/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;

@SuppressWarnings("rawtypes")
public abstract class KeyframeEvent<T> {
    private final T entity;
    private final double animationTick;
    private final AnimationController controller;

    public KeyframeEvent(T entity, double animationTick, AnimationController controller) {
        this.entity = entity;
        this.animationTick = animationTick;
        this.controller = controller;
    }

    public double getAnimationTick() {
        return animationTick;
    }

    public T getEntity() {
        return entity;
    }

    public AnimationController getController() {
        return controller;
    }
}
