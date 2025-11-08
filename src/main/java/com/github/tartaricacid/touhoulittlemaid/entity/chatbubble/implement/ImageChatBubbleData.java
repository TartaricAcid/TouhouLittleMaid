package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement.ImageChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ImageChatBubbleData implements IChatBubbleData {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "image");

    private final int existTick;
    private final ResourceLocation bg;
    private final ResourceLocation image;
    private final int width;
    private final int height;
    private final int uOffset;
    private final int vOffset;
    private final int textureWidth;
    private final int textureHeight;
    private final int priority;

    @OnlyIn(Dist.CLIENT)
    private IChatBubbleRenderer renderer;

    private ImageChatBubbleData(int existTick, ResourceLocation bg, ResourceLocation image, int width, int height,
                                int uOffset, int vOffset, int textureWidth, int textureHeight, int priority) {
        this.existTick = existTick;
        this.bg = bg;
        this.image = image;
        this.width = width;
        this.height = height;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.priority = priority;
    }

    public static ImageChatBubbleData create(ResourceLocation image, int width, int height) {
        return new ImageChatBubbleData(DEFAULT_EXIST_TICK, TYPE_2, image, width, height, 0, 0, 256, 256, DEFAULT_PRIORITY);
    }

    public static ImageChatBubbleData create(ResourceLocation image, int width, int height, int uOffset, int vOffset) {
        return new ImageChatBubbleData(DEFAULT_EXIST_TICK, TYPE_2, image, width, height, uOffset, vOffset, 256, 256, DEFAULT_PRIORITY);
    }

    public static ImageChatBubbleData singleImage(ResourceLocation image, int width, int height) {
        return new ImageChatBubbleData(DEFAULT_EXIST_TICK, TYPE_2, image, width, height, 0, 0, width, height, DEFAULT_PRIORITY);
    }

    public static ImageChatBubbleData create(int existTick, ResourceLocation bg, ResourceLocation image, int width, int height,
                                             int uOffset, int vOffset, int textureWidth, int textureHeight, int priority) {
        return new ImageChatBubbleData(existTick, bg, image, width, height, uOffset, vOffset, textureWidth, textureHeight, priority);
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
        if (this.renderer == null) {
            this.renderer = new ImageChatBubbleRenderer(this.width, this.height, this.uOffset, this.vOffset,
                    this.textureWidth, this.textureHeight, this.bg, this.image);
        }
        return this.renderer;
    }

    public static class ImageChatSerializer implements IChatBubbleData.ChatSerializer {
        @Override
        public IChatBubbleData readFromBuff(FriendlyByteBuf buf) {
            int width = buf.readVarInt();
            int height = buf.readVarInt();
            int uOffset = buf.readVarInt();
            int vOffset = buf.readVarInt();
            int textureWidth = buf.readVarInt();
            int textureHeight = buf.readVarInt();
            ResourceLocation bg = buf.readResourceLocation();
            ResourceLocation image = buf.readResourceLocation();
            return new ImageChatBubbleData(DEFAULT_EXIST_TICK, bg, image, width, height,
                    uOffset, vOffset, textureWidth, textureHeight, DEFAULT_PRIORITY);
        }

        @Override
        public void writeToBuff(FriendlyByteBuf buf, IChatBubbleData data) {
            ImageChatBubbleData imageChat = (ImageChatBubbleData) data;
            buf.writeVarInt(imageChat.width);
            buf.writeVarInt(imageChat.height);
            buf.writeVarInt(imageChat.uOffset);
            buf.writeVarInt(imageChat.vOffset);
            buf.writeVarInt(imageChat.textureWidth);
            buf.writeVarInt(imageChat.textureHeight);
            buf.writeResourceLocation(imageChat.bg);
            buf.writeResourceLocation(imageChat.image);
        }
    }
}
