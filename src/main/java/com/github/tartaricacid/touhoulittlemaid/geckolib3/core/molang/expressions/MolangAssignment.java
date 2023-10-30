package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.expressions;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.Variable;


public class MolangAssignment extends MolangExpression {
    public Variable variable;
    public IValue expression;

    public MolangAssignment(MolangParser context, Variable variable, IValue expression) {
        super(context);
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public double get() {
        double value = this.expression.get();
        this.variable.set(value);
        return value;
    }

    @Override
    public String toString() {
        return this.variable.getName() + " = " + this.expression.toString();
    }
}
