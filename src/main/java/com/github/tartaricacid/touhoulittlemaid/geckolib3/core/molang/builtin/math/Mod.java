package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.math;

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;

public class Mod implements Function {
    @Override
    public Object evaluate(ExecutionContext<?> context, ArgumentCollection arguments) {
        return arguments.getAsDouble(context, 0) % arguments.getAsDouble(context, 1);
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 2;
    }
}
