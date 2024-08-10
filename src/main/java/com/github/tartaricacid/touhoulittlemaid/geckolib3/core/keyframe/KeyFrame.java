/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing.EasingType;
import it.unimi.dsi.fastutil.doubles.DoubleLists;

import java.util.List;
import java.util.Objects;

public class KeyFrame<T> {
    public final EasingType easingType;
    public final List<Double> easingArgs;
    private final Double length;
    private final T startValue;
    private final T endValue;

    public KeyFrame(Double length, T startValue, T endValue) {
        this.length = length;
        this.startValue = startValue;
        this.endValue = endValue;
        this.easingType = EasingType.LINEAR;
        this.easingArgs = DoubleLists.emptyList();
    }

    public KeyFrame(Double length, T startValue, T endValue, EasingType easingType) {
        this.length = length;
        this.startValue = startValue;
        this.endValue = endValue;
        this.easingType = easingType;
        this.easingArgs = DoubleLists.emptyList();
    }

    public KeyFrame(Double length, T startValue, T endValue, EasingType easingType, List<Double> easingArgs) {
        this.length = length;
        this.startValue = startValue;
        this.endValue = endValue;
        this.easingType = easingType;
        this.easingArgs = easingArgs;
    }

    public Double getLength() {
        return length;
    }

    public T getStartValue() {
        return startValue;
    }

    public T getEndValue() {
        return endValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, startValue, endValue);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KeyFrame && hashCode() == obj.hashCode();
    }
}
