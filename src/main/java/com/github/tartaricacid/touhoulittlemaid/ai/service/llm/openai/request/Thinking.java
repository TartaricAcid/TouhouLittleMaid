package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.google.gson.annotations.SerializedName;

public class Thinking {
    @SerializedName("type")
    private final Type type;

    private Thinking(Type type) {
        this.type = type;
    }

    public static Thinking enabled() {
        return new Thinking(Type.ENABLED);
    }

    public static Thinking disabled() {
        return new Thinking(Type.DISABLED);
    }

    public enum Type {
        @SerializedName("enabled")
        ENABLED,
        @SerializedName("disabled")
        DISABLED
    }
}
