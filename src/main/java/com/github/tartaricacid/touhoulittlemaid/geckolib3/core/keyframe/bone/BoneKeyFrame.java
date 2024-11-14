package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import org.joml.Vector3f;

public abstract class BoneKeyFrame {
    protected final double startTick;
    protected final double totalTick;
    protected final Vector3v beginPoint;

    public BoneKeyFrame(double startTick, double totalTick, Vector3v beginPoint) {
        this.startTick = startTick;
        this.totalTick = totalTick;
        this.beginPoint = beginPoint;
    }

    public double getStartTick() {
        return startTick;
    }

    public double getTotalTick() {
        return totalTick;
    }

    public abstract Vector3f getLerpPoint(ExpressionEvaluator<?> evaluator, double percentCompleted);

    public Vector3f getTransitionPoint(ExpressionEvaluator<?> evaluator, Vector3f offsetPoint, double percentCompleted) {
        if (isBegin(percentCompleted)) {
            return offsetPoint;
        }
        if (isEnd(percentCompleted)) {
            return beginPoint.eval(evaluator);
        }
        return MathUtil.lerpValues(percentCompleted, offsetPoint, beginPoint.eval(evaluator));
    }

    protected static boolean isBegin(double percentCompleted) {
        return percentCompleted < 0.00001;
    }

    protected static boolean isEnd(double percentCompleted) {
        return percentCompleted > 0.99999;
    }
}
