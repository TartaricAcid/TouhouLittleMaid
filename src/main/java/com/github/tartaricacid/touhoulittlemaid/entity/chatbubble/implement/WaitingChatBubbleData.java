package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement.WaitingChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class WaitingChatBubbleData implements IChatBubbleData {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "waiting");

    private final int existTick;
    private final ResourceLocation bg;
    private final int priority;
    private final Component text;
    private final ResourceLocation icon;

    @OnlyIn(Dist.CLIENT)
    private IChatBubbleRenderer renderer;

    private WaitingChatBubbleData(int existTick, ResourceLocation bg, int priority, Component text, ResourceLocation icon) {
        this.existTick = existTick;
        this.bg = bg;
        this.priority = priority;
        this.text = text;
        this.icon = icon;
    }

    public static WaitingChatBubbleData create(int existTick, ResourceLocation bg, int priority, Component text, ResourceLocation icon) {
        return new WaitingChatBubbleData(existTick, bg, priority, text, icon);
    }

    public static WaitingChatBubbleData create(Component text, ResourceLocation icon) {
        return new WaitingChatBubbleData(DEFAULT_EXIST_TICK, TYPE_2, DEFAULT_PRIORITY, text, icon);
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public IChatBubbleRenderer getRenderer(IChatBubbleRenderer.Position position) {
        if (renderer == null) {
            renderer = new WaitingChatBubbleRenderer(this.bg, this.text, this.icon);
        }
        return renderer;
    }

    public static class WaitingChatSerializer implements IChatBubbleData.ChatSerializer {
        @Override
        public IChatBubbleData readFromBuff(FriendlyByteBuf buf) {
            // 往客户端同步的数据里，不需要同步 existTick 和 priority，这两个数据仅在服务端有效
            return new WaitingChatBubbleData(DEFAULT_EXIST_TICK, buf.readResourceLocation(), DEFAULT_PRIORITY, buf.readJsonWithCodec(ComponentSerialization.CODEC), buf.readResourceLocation());
        }

        @Override
        public void writeToBuff(FriendlyByteBuf buf, IChatBubbleData data) {
            WaitingChatBubbleData textChat = (WaitingChatBubbleData) data;
            buf.writeResourceLocation(textChat.bg);
            buf.writeJsonWithCodec(ComponentSerialization.CODEC, textChat.text);
            buf.writeResourceLocation(textChat.icon);
        }
    }
}
