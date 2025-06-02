package com.github.tartaricacid.touhoulittlemaid.ai.service.function;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.implement.SwitchAttackTaskFunction;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class FunctionCallRegister {
    private static Map<String, IFunctionCall<?>> FUNCTION_CALLS = Maps.newHashMap();

    public static void init() {
        FunctionCallRegister register = new FunctionCallRegister();
        register.register(new SwitchAttackTaskFunction());

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAIFunctionCall(register);
        }

        FUNCTION_CALLS = ImmutableMap.copyOf(FUNCTION_CALLS);
    }

    public void register(IFunctionCall<?> functionCall) {
        FUNCTION_CALLS.put(functionCall.getId(), functionCall);
    }

    @Nullable
    public static IFunctionCall<?> getFunctionCall(String name) {
        return FUNCTION_CALLS.get(name);
    }

    public static Map<String, IFunctionCall<?>> getFunctionCalls() {
        return FUNCTION_CALLS;
    }
}
