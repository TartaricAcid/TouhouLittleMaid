package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement.TextChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TextChatBubbleData implements IChatBubbleData {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "text");

    private final int existTick;
    private final ResourceLocation bg;
    private final int priority;
    private Component text;

    @OnlyIn(Dist.CLIENT)
    private IChatBubbleRenderer renderer;

    private TextChatBubbleData(int existTick, Component text, ResourceLocation bg, int priority) {
        this.existTick = existTick;
        this.text = text;
        this.bg = bg;
        this.priority = priority;
    }

    private TextChatBubbleData(int existTick, Component text, ResourceLocation bg) {
        this(existTick, text, bg, DEFAULT_PRIORITY);
    }

    public static TextChatBubbleData type1(Component text) {
        return new TextChatBubbleData(DEFAULT_EXIST_TICK, text, TYPE_1);
    }

    public static TextChatBubbleData type2(Component text) {
        return new TextChatBubbleData(DEFAULT_EXIST_TICK, text, TYPE_2);
    }

    public static TextChatBubbleData create(int existTick, Component text, ResourceLocation bg, int priority) {
        return new TextChatBubbleData(existTick, text, bg, priority);
    }

    @Override
    public int existTick() {
        return this.existTick;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public int priority() {
        return this.priority;
    }

    public void setText(Component text) {
        this.text = text;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IChatBubbleRenderer getRenderer(IChatBubbleRenderer.Position position) {
        if (renderer == null) {
            renderer = new TextChatBubbleRenderer(this.text, this.bg, position);
        }
        return renderer;
    }

    public static class TextChatSerializer implements IChatBubbleData.ChatSerializer {
        @Override
        public IChatBubbleData readFromBuff(FriendlyByteBuf buf) {
            // 往客户端同步的数据里，不需要同步 existTick 和 priority，这两个数据仅在服务端有效
            return new TextChatBubbleData(DEFAULT_EXIST_TICK, buf.readJsonWithCodec(ComponentSerialization.CODEC), buf.readResourceLocation());
        }

        @Override
        public void writeToBuff(FriendlyByteBuf buf, IChatBubbleData data) {
            TextChatBubbleData textChat = (TextChatBubbleData) data;
            buf.writeJsonWithCodec(ComponentSerialization.CODEC, textChat.text);
            buf.writeResourceLocation(textChat.bg);
        }
    }
}
