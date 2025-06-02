package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement.ProgressChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ProgressChatBubbleData implements IChatBubbleData {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "progress");

    private final int existTick;
    private final ResourceLocation bg;
    private final int priority;
    private final Component text;
    private final int barBackgroundColor;
    private final int barForegroundColor;
    private final double progress;
    private final boolean alignCenter;

    @OnlyIn(Dist.CLIENT)
    private IChatBubbleRenderer renderer;

    private ProgressChatBubbleData(int existTick, ResourceLocation bg, int priority, Component text, int barBackgroundColor,
                                   int barForegroundColor, double progress, boolean alignCenter) {
        this.existTick = existTick;
        this.bg = bg;
        this.priority = priority;
        this.text = text;
        this.barBackgroundColor = barBackgroundColor;
        this.barForegroundColor = barForegroundColor;
        this.progress = progress;
        this.alignCenter = alignCenter;
    }

    public static ProgressChatBubbleData create(int existTick, ResourceLocation bg, int priority, Component text,
                                                int barBackgroundColor, int barForegroundColor, double progress,
                                                boolean alignCenter) {
        return new ProgressChatBubbleData(existTick, bg, priority, text, barBackgroundColor, barForegroundColor,
                progress, alignCenter);
    }

    public static ProgressChatBubbleData create(Component text, int barBackgroundColor, int barForegroundColor, double progress, boolean alignCenter) {
        return new ProgressChatBubbleData(DEFAULT_EXIST_TICK, TYPE_2, DEFAULT_PRIORITY, text, barBackgroundColor,
                barForegroundColor, progress, alignCenter);
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
            renderer = new ProgressChatBubbleRenderer(this.bg, this.text, this.barBackgroundColor, this.barForegroundColor, this.progress, this.alignCenter);
        }
        return renderer;
    }

    public static class ProgressChatSerializer implements IChatBubbleData.ChatSerializer {
        @Override
        public IChatBubbleData readFromBuff(FriendlyByteBuf buf) {
            // 往客户端同步的数据里，不需要同步 existTick 和 priority，这两个数据仅在服务端有效
            return new ProgressChatBubbleData(DEFAULT_EXIST_TICK, buf.readResourceLocation(), DEFAULT_PRIORITY, buf.readJsonWithCodec(ComponentSerialization.CODEC),
                    buf.readInt(), buf.readInt(), buf.readDouble(), buf.readBoolean());
        }

        @Override
        public void writeToBuff(FriendlyByteBuf buf, IChatBubbleData data) {
            ProgressChatBubbleData textChat = (ProgressChatBubbleData) data;
            buf.writeResourceLocation(textChat.bg);
            buf.writeJsonWithCodec(ComponentSerialization.CODEC, textChat.text);
            buf.writeInt(textChat.barBackgroundColor);
            buf.writeInt(textChat.barForegroundColor);
            buf.writeDouble(textChat.progress);
            buf.writeBoolean(textChat.alignCenter);
        }
    }
}
