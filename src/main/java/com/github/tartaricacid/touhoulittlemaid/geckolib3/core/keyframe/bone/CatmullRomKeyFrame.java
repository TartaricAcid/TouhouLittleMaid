package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import org.joml.Vector3f;

public class CatmullRomKeyFrame extends BoneKeyFrame {
    private final Vector3v leftPoint;
    private final Vector3v endPoint;
    private final Vector3v rightPoint;
    private final Vector3v postPoint;

    public CatmullRomKeyFrame(double startTick, double totalTick, Vector3v leftPoint, Vector3v current, Vector3v endPoint, Vector3v postRight, Vector3v postPoint) {
        super(startTick, totalTick, current);
        this.leftPoint = leftPoint;
        this.endPoint = endPoint;
        this.rightPoint = postRight;
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
        Vector3f left = leftPoint.eval(evaluator);
        Vector3f begin = beginPoint.eval(evaluator);
        Vector3f end = endPoint.eval(evaluator);
        Vector3f right = rightPoint.eval(evaluator);
        return MathUtil.catmullRom(percentCompleted, left, begin, end, right);
    }
}