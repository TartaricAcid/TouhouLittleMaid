package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ChatTextType {
    // 图标
    ICON,
    // 普通文本
    TEXT,
    // 空
    EMPTY;
    public static final IntFunction<ChatTextType> BY_ID =
            ByIdMap.continuous(
                    ChatTextType::ordinal,
                    ChatTextType.values(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );
    public static final StreamCodec<ByteBuf, ChatTextType> STREAM_CODEC = ByteBufCodecs.idMapper(ChatTextType.BY_ID, ChatTextType::ordinal);
}