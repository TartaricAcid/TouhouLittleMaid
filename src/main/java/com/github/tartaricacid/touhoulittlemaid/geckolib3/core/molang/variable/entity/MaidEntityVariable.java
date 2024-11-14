package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.IValueEvaluator;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.LambdaVariable;

public class MaidEntityVariable extends LambdaVariable<EntityMaid> {
    public MaidEntityVariable(IValueEvaluator<?, IContext<EntityMaid>> evaluator) {
        super(evaluator);
    }

    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof EntityMaid;
    }
}
