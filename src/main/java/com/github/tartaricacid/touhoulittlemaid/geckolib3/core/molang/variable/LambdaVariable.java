package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;

public class LambdaVariable<TTarget> extends ContextVariable<TTarget> {
    private final IValueEvaluator<?, IContext<TTarget>> evaluator;

    public LambdaVariable(IValueEvaluator<?, IContext<TTarget>> evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public final Object evaluate(IContext<TTarget> entityContext) {
        return evaluator.eval(entityContext);
    }
}
