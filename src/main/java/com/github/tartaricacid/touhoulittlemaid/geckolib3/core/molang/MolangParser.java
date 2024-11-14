package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.PrimaryBinding;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.DoubleValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.MolangValue;
import com.github.tartaricacid.touhoulittlemaid.molang.MolangEngine;
import com.github.tartaricacid.touhoulittlemaid.molang.parser.ParseException;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;

import java.util.Map;

public class MolangParser {
    private final MolangEngine engine;
    private final PrimaryBinding primaryBinding;

    public MolangParser(Map<String, ObjectBinding> extraBindings) {
        primaryBinding = new PrimaryBinding(extraBindings);
        engine = MolangEngine.fromCustomBinding(primaryBinding);
    }

    @SuppressWarnings("unused")
    public IValue parseExpression(String molangExpression) {
        try {
            return parseExpressionUnsafe(molangExpression);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to parse value \"{}\": {}", molangExpression, e.getMessage());
            return DoubleValue.ZERO;
        }
    }

    public IValue parseExpressionUnsafe(String molangExpression) throws ParseException {
        MolangValue value = new MolangValue(engine.parse(molangExpression));
        primaryBinding.popStackFrame();
        return value;
    }

    @SuppressWarnings("unused")
    public IValue getConstant(double value) {
        return new DoubleValue(value);
    }

    public void reset() {
        primaryBinding.reset();
    }
}
