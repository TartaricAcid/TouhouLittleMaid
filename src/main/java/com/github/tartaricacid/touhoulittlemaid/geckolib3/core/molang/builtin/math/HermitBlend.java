package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.math;

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;

public class HermitBlend implements Function {
    @Override
    public Object evaluate(ExecutionContext<?> context, ArgumentCollection arguments) {
        double min = Math.ceil(arguments.getAsDouble(context, 0));
        return Math.floor(3.0 * Math.pow(min, 2.0) - 2.0 * Math.pow(min, 3.0));
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}
