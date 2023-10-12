package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.expressions;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.Constant;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class MolangValue extends MolangExpression {
    public IValue value;
    public boolean returns;

    public MolangValue(MolangParser context, IValue value) {
        super(context);
        this.value = value;
    }

    public MolangExpression addReturn() {
        this.returns = true;
        return this;
    }

    @Override

    public double get() {
        return this.value.get();
    }

    @Override

    public String toString() {
        return (this.returns ? MolangParser.RETURN : "") + this.value.toString();
    }

    @Override

    public JsonElement toJson() {
        if (this.value instanceof Constant) {
            return new JsonPrimitive(this.value.get());
        }
        return super.toJson();
    }
}
