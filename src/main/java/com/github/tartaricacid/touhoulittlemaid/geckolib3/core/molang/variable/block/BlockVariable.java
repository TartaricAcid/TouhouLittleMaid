package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.block;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.IValueEvaluator;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.level.block.Block;

public class BlockVariable extends LambdaVariable<Block> {
    public BlockVariable(IValueEvaluator<?, IContext<Block>> evaluator) {
        super(evaluator);
    }

    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Block;
    }
}
