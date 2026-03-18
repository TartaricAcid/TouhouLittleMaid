package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;

import java.util.Collection;

public final class ToolErrorHelper {
    private ToolErrorHelper() {
    }

    public static ToolResponse invalidParamToolResponse(String parameterName, Collection<String> values, String reason) {
        String joined = String.join(", ", values);
        String correctUsage = "%s: choose one of [%s]".formatted(parameterName, joined);
        String text = """
                Invalid tool parameters: %s
                Correct parameters:
                %s
                """.formatted(reason, correctUsage);
        return new ToolResponse(text);
    }
}
