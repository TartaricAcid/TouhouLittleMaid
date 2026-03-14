package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.google.gson.annotations.SerializedName;

public class DoubaoThinking {
    @SerializedName("type")
    private final Type type;

    private DoubaoThinking(Type type) {
        this.type = type;
    }

    public static DoubaoThinking enabled() {
        return new DoubaoThinking(Type.ENABLED);
    }

    public static DoubaoThinking disabled() {
        return new DoubaoThinking(Type.DISABLED);
    }

    public static DoubaoThinking auto() {
        return new DoubaoThinking(Type.AUTO);
    }

    public enum Type {
        @SerializedName("enabled")
        ENABLED,
        @SerializedName("disabled")
        DISABLED,
        @SerializedName("auto")
        AUTO
    }
}
