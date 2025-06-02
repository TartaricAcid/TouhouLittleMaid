package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

public enum LLMApiType {
    OPENAI("openai");

    private final String name;

    LLMApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
