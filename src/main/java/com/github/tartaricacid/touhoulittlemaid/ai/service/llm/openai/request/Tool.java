package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.FunctionTool;
import com.google.gson.annotations.SerializedName;

public class Tool {
    /**
     * 一般情况下都是这个值，我们一般不使用模型内置的 tool
     */
    @SerializedName("type")
    private String type = "function";

    @SerializedName("function")
    private FunctionTool function;

    public Tool(FunctionTool function) {
        this.function = function;
    }
}
