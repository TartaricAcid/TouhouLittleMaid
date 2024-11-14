package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import org.joml.Vector3f;

public class Vector3v {
    private final IValue x;
    private final IValue y;
    private final IValue z;

    public Vector3v(IValue x, IValue y, IValue z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f eval(ExpressionEvaluator<?> evaluator) {
        return new Vector3f((float) x.evalAsDouble(evaluator),
                (float) y.evalAsDouble(evaluator),
                (float) z.evalAsDouble(evaluator));
    }
}
