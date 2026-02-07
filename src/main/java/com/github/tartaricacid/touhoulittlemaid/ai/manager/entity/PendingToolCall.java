package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * 用于在chatManager里记录调用且未完成的tool call
 */
public record PendingToolCall(String toolCallId, int callCount, long waitingChatBubbleId) {
    public static final Codec<PendingToolCall> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("tool_call_id").forGetter(PendingToolCall::toolCallId),
            Codec.INT.fieldOf("call_count").forGetter(PendingToolCall::callCount),
            Codec.LONG.fieldOf("waiting_bubble_id").forGetter(PendingToolCall::waitingChatBubbleId)
    ).apply(instance, PendingToolCall::new));
}
