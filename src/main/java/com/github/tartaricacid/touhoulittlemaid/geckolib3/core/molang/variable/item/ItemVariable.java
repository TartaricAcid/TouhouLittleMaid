package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.item;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.IValueEvaluator;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.LambdaVariable;
import net.minecraft.world.item.Item;

public class ItemVariable extends LambdaVariable<Item> {
    public ItemVariable(IValueEvaluator<?, IContext<Item>> evaluator) {
        super(evaluator);
    }

    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Item;
    }
}
