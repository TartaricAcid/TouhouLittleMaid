package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import org.joml.Vector3f;

public class LinearKeyFrame extends BoneKeyFrame {
    private final Vector3v endPoint;
    private final Vector3v postPoint;

    public LinearKeyFrame(double startTick, double totalTick, Vector3v beginPoint, Vector3v endPoint, Vector3v postPoint) {
        super(startTick, totalTick, beginPoint);
        this.endPoint = endPoint;
        this.postPoint = postPoint;
    }

    @Override
    public Vector3f getLerpPoint(ExpressionEvaluator<?> evaluator, double percentCompleted) {
        if (isBegin(percentCompleted)) {
            return beginPoint.eval(evaluator);
        }
        if (isEnd(percentCompleted)) {
            return postPoint.eval(evaluator);
        }
        Vector3f begin = beginPoint.eval(evaluator);
        Vector3f end = endPoint.eval(evaluator);
        return MathUtil.lerpValues(percentCompleted, begin, end);
    }
}
