package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.request;

public enum Role {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool"),
    FUNCTION("function");

    private final String id;

    Role(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
