package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.limit;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;
import net.minecraft.util.Mth;

public class Clamp extends Function {
    public Clamp(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override

    public int getRequiredArguments() {
        return 3;
    }

    @Override

    public double get() {
        return Mth.clamp(this.getArg(0), this.getArg(1), this.getArg(2));
    }
}
