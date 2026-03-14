package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

public enum LLMApiType {
    OPENAI("openai"),
    /**
     * 豆包模型添加了不属于 openai api 规范的部分字段，故需要单独列出
     */
    DOUBAO("doubao");

    private final String name;

    LLMApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
