package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.classic;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class Cos extends Function {
    public Cos(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override

    public int getRequiredArguments() {
        return 1;
    }

    @Override

    public double get() {
        return Math.cos(this.getArg(0));
    }
}
