package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import org.apache.commons.lang3.tuple.Pair;

public class MaidChatBubbles {
    public static final Pair<Long, ChatText> EMPTY = Pair.of(-1L, ChatText.EMPTY_CHAT_TEXT);
    public static final MaidChatBubbles DEFAULT = new MaidChatBubbles(EMPTY, EMPTY, EMPTY);
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

    public static final IDataSerializer<MaidChatBubbles> DATA = new IDataSerializer<MaidChatBubbles>() {
        @Override
        public void write(PacketBuffer buf, MaidChatBubbles value) {
            buf.writeLong(value.getBubble1().getLeft());
            ChatText.toBuff(value.getBubble1().getRight(), buf);
            buf.writeLong(value.getBubble2().getLeft());
            ChatText.toBuff(value.getBubble2().getRight(), buf);
            buf.writeLong(value.getBubble3().getLeft());
            ChatText.toBuff(value.getBubble3().getRight(), buf);
        }

        @Override
        public MaidChatBubbles read(PacketBuffer buf) {
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
}
