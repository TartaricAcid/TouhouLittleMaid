package com.github.tartaricacid.touhoulittlemaid.ai.service.chat;

public enum ChatApiType {
    OPENAI("openai");

    private final String name;

    ChatApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
