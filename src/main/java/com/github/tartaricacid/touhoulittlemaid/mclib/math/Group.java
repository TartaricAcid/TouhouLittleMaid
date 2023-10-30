package com.github.tartaricacid.touhoulittlemaid.mclib.math;


public class Group implements IValue {
    private IValue value;

    public Group(IValue value) {
        this.value = value;
    }

    @Override
    public double get() {
        return this.value.get();
    }

    @Override
    public String toString() {
        return "(" + this.value.toString() + ")";
    }
}
