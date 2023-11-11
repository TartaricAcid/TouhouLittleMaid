package com.github.tartaricacid.touhoulittlemaid.geckolib3.core;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;

public class ConstantValue implements IValue {
    private final double value;

    public ConstantValue(double value) {
        this.value = value;
    }

    public static ConstantValue fromDouble(double d) {
        return new ConstantValue(d);
    }

    public static ConstantValue fromFloat(float d) {
        return new ConstantValue(d);
    }

    public static ConstantValue parseDouble(String s) {
        return new ConstantValue(Double.parseDouble(s));
    }

    public static ConstantValue parseFloat(String s) {
        return new ConstantValue(Float.parseFloat(s));
    }

    public static ConstantValue subtract(IValue first, IValue second) {
        return ConstantValue.fromDouble(first.get() - second.get());
    }

    @Override
    public double get() {
        return value;
    }
}
