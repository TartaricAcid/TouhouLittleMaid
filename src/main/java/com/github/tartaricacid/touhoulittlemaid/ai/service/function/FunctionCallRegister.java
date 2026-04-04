package com.github.tartaricacid.touhoulittlemaid.ai.service.function;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * @deprecated 自 1.5.1 起，更换为 skill 机制
 */
@Deprecated(since = "1.5.1")
@SuppressWarnings("removal")
public class FunctionCallRegister {
    private static Map<String, IFunctionCall<?>> FUNCTION_CALLS = Collections.emptyMap();

    public static void init() {
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
