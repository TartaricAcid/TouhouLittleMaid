package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.apache.commons.lang3.tuple.Pair;

public class MaidChatBubbles {
    public static final Pair<Long, ChatText> EMPTY = Pair.of(-1L, ChatText.EMPTY_CHAT_TEXT);
    public static final MaidChatBubbles DEFAULT = new MaidChatBubbles(EMPTY, EMPTY, EMPTY);
    public static final EntityDataSerializer<MaidChatBubbles> DATA = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, MaidChatBubbles value) {
            buf.writeLong(value.getBubble1().getLeft());
            ChatText.toBuff(value.getBubble1().getRight(), buf);
            buf.writeLong(value.getBubble2().getLeft());
            ChatText.toBuff(value.getBubble2().getRight(), buf);
            buf.writeLong(value.getBubble3().getLeft());
            ChatText.toBuff(value.getBubble3().getRight(), buf);
        }

        @Override
        public MaidChatBubbles read(FriendlyByteBuf buf) {
            Pair<Long, ChatText> bubble1 = Pair.of(buf.readLong(), ChatText.fromBuff(buf));
            Pair<Long, ChatText> bubble2 = Pair.of(buf.readLong(), ChatText.fromBuff(buf));
            Pair<Long, ChatText> bubble3 = Pair.of(buf.readLong(), ChatText.fromBuff(buf));
            return new MaidChatBubbles(bubble1, bubble2, bubble3);
        }

        @Override
        public MaidChatBubbles copy(MaidChatBubbles value) {
            return value;
        }
    };
    private final Pair<Long, ChatText> bubble1;
    private final Pair<Long, ChatText> bubble2;
    private final Pair<Long, ChatText> bubble3;

    public MaidChatBubbles(Pair<Long, ChatText> bubble1, Pair<Long, ChatText> bubble2, Pair<Long, ChatText> bubble3) {
        this.bubble1 = bubble1;
        this.bubble2 = bubble2;
        this.bubble3 = bubble3;
    }

    public Pair<Long, ChatText> getBubble1() {
        return bubble1;
    }

    public Pair<Long, ChatText> getBubble2() {
        return bubble2;
    }

    public Pair<Long, ChatText> getBubble3() {
        return bubble3;
    }
}