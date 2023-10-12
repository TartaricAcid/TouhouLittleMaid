package com.github.tartaricacid.touhoulittlemaid.mclib.math;


public class Operator implements IValue {
    public Operation operation;
    public IValue a;
    public IValue b;

    public Operator(Operation op, IValue a, IValue b) {
        this.operation = op;
        this.a = a;
        this.b = b;
    }

    @Override

    public double get() {
        return this.operation.calculate(a.get(), b.get());
    }

    @Override

    public String toString() {
        return a.toString() + " " + this.operation.sign + " " + b.toString();
    }
}
