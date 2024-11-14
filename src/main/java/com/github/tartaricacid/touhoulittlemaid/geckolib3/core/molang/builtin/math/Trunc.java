package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.math;

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;

public class Trunc implements Function {
    @Override
    public Object evaluate(ExecutionContext<?> context, ArgumentCollection arguments) {
        double value = arguments.getAsDouble(context, 0);
        return value < 0 ? Math.ceil(value) : Math.floor(value);
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}
