package com.github.tartaricacid.touhoulittlemaid.ai.service.function.response;

/**
 * 当本地代码执行完 Function Call 时，返回的对象
 */
public record ToolResponse(String message) {
    /**
     * IFunctionCall 返回这个来表示它在之后会告知chatManager这个tool call跑完了
     */
    public static final ToolResponse PENDING = new ToolResponse(null);

    public boolean isPending() {
        return this == PENDING;
    }
}
