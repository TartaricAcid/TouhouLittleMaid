package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.functions;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class CosDegrees extends Function {
    public CosDegrees(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override

    public int getRequiredArguments() {
        return 1;
    }

    @Override

    public double get() {
        return Math.cos(this.getArg(0) / 180 * Math.PI);
    }
}
