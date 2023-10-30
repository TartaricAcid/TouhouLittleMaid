package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.utility;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;


public class DieRollInteger extends Function {
    public java.util.Random random;

    public DieRollInteger(IValue[] values, String name) throws Exception {
        super(values, name);
        this.random = new java.util.Random();
    }

    @Override
        public int getRequiredArguments() {
        return 3;
    }

    @Override
        public double get() {
        double i = 0;
        double total = 0;
        while (i < this.getArg(0)) {
            total += (double) Math.round(this.getArg(1) + Math.random() * (this.getArg(2) - this.getArg(1)));
        }
        return total;
    }
}
