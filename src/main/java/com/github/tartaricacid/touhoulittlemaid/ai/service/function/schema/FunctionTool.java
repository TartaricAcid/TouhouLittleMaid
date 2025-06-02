package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.Tool;
import com.google.gson.annotations.SerializedName;

public class FunctionTool {
    @SerializedName("type")
    private String type = "function";

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("parameters")
    private Parameter parameters;

    @SerializedName("strict")
    private boolean strict = false;

    private FunctionTool() {
    }

    public static FunctionTool create() {
        return new FunctionTool();
    }

    public FunctionTool setName(String name) {
        this.name = name;
        return this;
    }

    public FunctionTool setDescription(String description) {
        this.description = description;
        return this;
    }

    public FunctionTool setParameters(Parameter parameters) {
        this.parameters = parameters;
        return this;
    }

    public Tool build() {
        return new Tool(this);
    }
}
