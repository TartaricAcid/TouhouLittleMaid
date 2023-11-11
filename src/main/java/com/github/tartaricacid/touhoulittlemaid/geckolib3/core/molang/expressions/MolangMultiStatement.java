package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.expressions;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.LazyVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MolangMultiStatement extends MolangExpression {
    public final List<MolangExpression> expressions = new ObjectArrayList<>();
    public final Map<String, LazyVariable> locals = new Object2ObjectOpenHashMap<>();

    public MolangMultiStatement(MolangParser context) {
        super(context);
    }

    @Override
    public double get() {
        double value = 0;
        for (MolangExpression expression : this.expressions) {
            value = expression.get();
        }
        return value;
    }

    @Override
    public String toString() {
        StringJoiner builder = new StringJoiner("; ");
        for (MolangExpression expression : this.expressions) {
            builder.add(expression.toString());
            if (expression instanceof MolangValue && ((MolangValue) expression).returns) {
                break;
            }
        }
        return builder.toString();
    }
}
