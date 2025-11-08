package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleDataCollection.MAX_SIZE;

public class ChatBubbleRegister {
    public static Map<ResourceLocation, IChatBubbleData.ChatSerializer> CODEC_MAP = Maps.newHashMap();
    public static final EntityDataSerializer<ChatBubbleDataCollection> INSTANCE = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ChatBubbleDataCollection> codec() {
            return new StreamCodec<>() {
                @Override
                public void encode(RegistryFriendlyByteBuf buf, ChatBubbleDataCollection data) {
                    buf.writeVarInt(Math.min(MAX_SIZE, data.size()));
                    int i = 0;
                    for (long key : data.keySet()) {
                        if (i < MAX_SIZE) {
                            IChatBubbleData bubbleData = data.get(key);
                            buf.writeLong(key);
                            ResourceLocation id = bubbleData.id();
                            buf.writeResourceLocation(id);
                            ChatBubbleRegister.CODEC_MAP.get(id).writeToBuff(buf, bubbleData);
                        }
                        i++;
                    }
                }

                @Override
                public ChatBubbleDataCollection decode(RegistryFriendlyByteBuf buf) {
                    ChatBubbleDataCollection map = new ChatBubbleDataCollection(new Long2ObjectAVLTreeMap<>());
                    int size = buf.readVarInt();
                    for (int i = 0; i < size; i++) {
                        long key = buf.readLong();
                        ResourceLocation id = buf.readResourceLocation();
                        IChatBubbleData bubbleData = ChatBubbleRegister.CODEC_MAP.get(id).readFromBuff(buf);
                        map.put(key, bubbleData);
                    }
                    return map;
                }
            };
        }

        @Override
        public ChatBubbleDataCollection copy(ChatBubbleDataCollection value) {
            return value;
        }
    };

    public static void init() {
        ChatBubbleRegister register = new ChatBubbleRegister();
        register.register(TextChatBubbleData.ID, new TextChatBubbleData.TextChatSerializer());
        register.register(ImageChatBubbleData.ID, new ImageChatBubbleData.ImageChatSerializer());
        register.register(WaitingChatBubbleData.ID, new WaitingChatBubbleData.WaitingChatSerializer());
        register.register(ProgressChatBubbleData.ID, new ProgressChatBubbleData.ProgressChatSerializer());
        register.register(EmojiChatBubbleData.ID, new EmojiChatBubbleData.EmojiChatSerializer());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerChatBubble(register);
        }
        CODEC_MAP = ImmutableMap.copyOf(CODEC_MAP);
    }

    public void register(ResourceLocation id, IChatBubbleData.ChatSerializer serializer) {
        if (CODEC_MAP.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate codec id: " + id);
        }
        CODEC_MAP.put(id, serializer);
    }
}
