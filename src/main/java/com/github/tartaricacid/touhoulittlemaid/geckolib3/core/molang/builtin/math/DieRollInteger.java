package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.math;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.ContextFunction;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;

import java.util.Random;

public class DieRollInteger extends ContextFunction<Object> {
    @Override
    public boolean validateArgumentSize(int size) {
        return size == 3;
    }

    @Override
    protected Object eval(ExecutionContext<IContext<Object>> context, ArgumentCollection arguments) {
        int i = Math.round(arguments.getAsFloat(context, 0));
        int min = arguments.getAsInt(context, 1);
        int range = arguments.getAsInt(context, 2);
        if(min > range) {
            int temp = min;
            min = range;
            range = temp - range;
        } else {
            range -= min;
        }
        int total = 0;
        Random rnd = context.entity().random();
        while (i-- > 0) {
            total += min + rnd.nextInt(range);
        }
        return total;
    }
}
