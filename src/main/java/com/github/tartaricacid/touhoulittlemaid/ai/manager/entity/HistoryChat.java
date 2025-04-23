package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.request.Role;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public record HistoryChat(Role role, String message, long gameTime) {
    public static HistoryChat userChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new HistoryChat(Role.USER, message, time);
    }

    public static HistoryChat assistantChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new HistoryChat(Role.ASSISTANT, message, time);
    }
}