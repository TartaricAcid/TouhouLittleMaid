package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;


import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * 大语言模型消息
 *
 * @param role     消息角色
 * @param message  消息内容
 * @param gameTime 游戏时间，目前暂时没有用途
 */
public record LLMMessage(Role role, String message, long gameTime, @Nullable List<ToolCall> toolCalls,
                         @Nullable String toolCallId) {
    public static Codec<LLMMessage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Role.CODEC.fieldOf("role").forGetter(LLMMessage::role),
            Codec.STRING.optionalFieldOf("message").forGetter(s -> Optional.ofNullable(s.message)),
            Codec.LONG.fieldOf("game_time").forGetter(LLMMessage::gameTime),
            ToolCall.CODEC.listOf().optionalFieldOf("tool_calls").forGetter(s -> Optional.ofNullable(s.toolCalls)),
            Codec.STRING.optionalFieldOf("tool_call_id").forGetter(s -> Optional.ofNullable(s.toolCallId))
    ).apply(instance, (role, message, gameTime, toolCalls, toolCallId)
            -> new LLMMessage(role, message.orElse(StringUtils.EMPTY), gameTime, toolCalls.orElse(null), toolCallId.orElse(null))));

    public static LLMMessage userChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.USER, message, time, null, null);
    }

    public static LLMMessage assistantChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.ASSISTANT, message, time, null, null);
    }

    public static LLMMessage assistantChat(EntityMaid maid, String message, List<ToolCall> toolCalls) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.ASSISTANT, message, time, toolCalls, null);
    }

    public static LLMMessage systemChat(EntityMaid maid, String message) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.SYSTEM, message, time, null, null);
    }

    public static LLMMessage toolChat(EntityMaid maid, String message, String toolCallId) {
        long time = maid.level.getGameTime();
        return new LLMMessage(Role.TOOL, message, time, null, toolCallId);
    }
}