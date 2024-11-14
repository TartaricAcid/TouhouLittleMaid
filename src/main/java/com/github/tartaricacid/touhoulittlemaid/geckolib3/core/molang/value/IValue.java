package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ValueConversions;

public interface IValue {
    /**
     * 依次执行表达式，返回最后一个表达式的值，或第一个 return 语句的值，并转换为 double 类型。
     */
    default double evalAsDouble(ExpressionEvaluator<?> evaluator) {
        try {
            Object result = evalUnsafe(evaluator);
            double value = ValueConversions.asDouble(result);
            if (!Double.isNaN(value)) {
                return value;
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to evaluate molang value.", e);
        }
        return 0;
    }

    /**
     * 依次执行表达式，返回最后一个表达式的值，或第一个 return 语句的值，并转换为 boolean 类型。
     */
    default boolean evalAsBoolean(ExpressionEvaluator<?> evaluator) {
        try {
            Object result = evalUnsafe(evaluator);
            return ValueConversions.asBoolean(result);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to evaluate molang value.", e);
        }
        return false;
    }

    /**
     * 依次执行表达式，返回最后一个表达式的值，或第一个 return 语句的值，返回值的类型不确定，并且可能抛出异常。
     */
    Object evalUnsafe(ExpressionEvaluator<?> evaluator) throws Exception;
}
