package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class FunctionToolCall {
    public static Codec<FunctionToolCall> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("name").forGetter(s -> Optional.ofNullable(s.name)),
            Codec.STRING.optionalFieldOf("arguments").forGetter(s -> Optional.ofNullable(s.arguments))
    ).apply(instance, (name, arguments) -> new FunctionToolCall(name.orElse(null), arguments.orElse(null))));

    @SerializedName("name")
    private String name;

    @SerializedName("arguments")
    private String arguments;

    public FunctionToolCall(String name, String arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public String getArguments() {
        return arguments;
    }
}
