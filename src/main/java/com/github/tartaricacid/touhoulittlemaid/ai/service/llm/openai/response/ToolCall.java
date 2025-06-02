package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class ToolCall {
    public static Codec<ToolCall> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("id").forGetter(s -> Optional.ofNullable(s.id)),
            FunctionToolCall.CODEC.optionalFieldOf("function").forGetter(s -> Optional.ofNullable(s.functionToolCall))
    ).apply(instance, (id, call) -> new ToolCall(id.orElse(null), call.orElse(null))));

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type = "function";

    @SerializedName("function")
    private FunctionToolCall functionToolCall;

    public ToolCall(String id, FunctionToolCall functionToolCall) {
        this.id = id;
        this.functionToolCall = functionToolCall;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public FunctionToolCall getFunction() {
        return functionToolCall;
    }
}
