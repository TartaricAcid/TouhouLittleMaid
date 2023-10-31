package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.classic;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class ATan2 extends Function {
    public ATan2(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
    public int getRequiredArguments() {
        return 2;
    }

    @Override
    public double get() {
        return Math.atan2(getArg(0), getArg(1));
    }
}
