package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.classic;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class Ln extends Function {
    public Ln(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
        public int getRequiredArguments() {
        return 1;
    }

    @Override
        public double get() {
        return Math.log(this.getArg(0));
    }
}
