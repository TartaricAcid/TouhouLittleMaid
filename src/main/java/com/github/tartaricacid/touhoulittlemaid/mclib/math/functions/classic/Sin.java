package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.classic;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class Sin extends Function {
    public Sin(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
        public int getRequiredArguments() {
        return 1;
    }

    @Override
        public double get() {
        return Math.sin(this.getArg(0));
    }
}
