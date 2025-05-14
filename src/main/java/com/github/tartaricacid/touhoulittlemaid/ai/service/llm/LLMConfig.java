package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

public record LLMConfig(String model, double temperature, int maxTokens) {
}
