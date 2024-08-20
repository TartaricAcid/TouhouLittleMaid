package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.apache.commons.lang3.tuple.Pair;

public record MaidChatBubbles(Pair<Long, ChatText> bubble1, Pair<Long, ChatText> bubble2,
                              Pair<Long, ChatText> bubble3) {
    public static final Pair<Long, ChatText> EMPTY = Pair.of(-1L, ChatText.EMPTY_CHAT_TEXT);
    public static final MaidChatBubbles DEFAULT = new MaidChatBubbles(EMPTY, EMPTY, EMPTY);
    public static final EntityDataSerializer<MaidChatBubbles> DATA = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, MaidChatBubbles> codec() {
            return STREAM_CODEC;
        }

        @Override
        public MaidChatBubbles copy(MaidChatBubbles value) {
            return value;
        }
    };

    private static final StreamCodec<ByteBuf, Pair<Long, ChatText>> PAIR_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, Pair::getLeft,
            ChatText.STREAM_CODEC, Pair::getRight,
            Pair::of
    );

    public static final StreamCodec<ByteBuf, MaidChatBubbles> STREAM_CODEC = StreamCodec.composite(
            PAIR_STREAM_CODEC,
            MaidChatBubbles::bubble1,
            PAIR_STREAM_CODEC,
            MaidChatBubbles::bubble2,
            PAIR_STREAM_CODEC,
            MaidChatBubbles::bubble3,
            MaidChatBubbles::new
    );
}