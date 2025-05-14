package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public record LLMMessage(Role role, String message, long gameTime) {
    public static LLMMessage userChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.USER, message, time);
    }

    public static LLMMessage assistantChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.ASSISTANT, message, time);
    }

    public static LLMMessage systemChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.SYSTEM, message, time);
    }
}