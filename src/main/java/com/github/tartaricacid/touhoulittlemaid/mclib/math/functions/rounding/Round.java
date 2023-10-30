package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.rounding;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class Round extends Function {
    public Round(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
        public int getRequiredArguments() {
        return 1;
    }

    @Override
        public double get() {
        return Math.round(this.getArg(0));
    }
}
