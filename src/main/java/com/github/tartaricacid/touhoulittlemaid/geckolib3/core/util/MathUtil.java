package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing.EasingManager;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing.EasingType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.AnimationPoint;

import java.util.function.Function;

@SuppressWarnings("all")
public class MathUtil {
    /**
     * 对一个 AnimationPoint 进行线性插值计算
     *
     * @param animationPoint 动画信息
     * @return 线性插值
     */
    public static float lerpValues(AnimationPoint animationPoint, EasingType easingType, Function<Double, Double> customEasingMethod) {
        if (animationPoint.currentTick >= animationPoint.animationEndTick) {
            return (float) animationPoint.animationEndValue;
        }
        if (animationPoint.currentTick == 0 && animationPoint.animationEndTick == 0) {
            return (float) animationPoint.animationEndValue;
        }
        if (easingType == EasingType.CUSTOM && customEasingMethod != null) {
            return lerpValues(customEasingMethod.apply(animationPoint.currentTick / animationPoint.animationEndTick),
                    animationPoint.animationStartValue, animationPoint.animationEndValue);
        } else if (easingType == EasingType.NONE && animationPoint.keyframe != null) {
            easingType = animationPoint.keyframe.easingType;
        }
        double ease = EasingManager.ease(animationPoint.currentTick / animationPoint.animationEndTick, easingType,
                animationPoint.keyframe == null ? null : animationPoint.keyframe.easingArgs);
        return lerpValues(ease, animationPoint.animationStartValue, animationPoint.animationEndValue);
    }

    public static float lerpValues(double percentCompleted, double startValue, double endValue) {
        return (float) lerp(percentCompleted, startValue, endValue);
    }

    public static double lerp(double pct, double start, double end) {
        return start + pct * (end - start);
    }
}