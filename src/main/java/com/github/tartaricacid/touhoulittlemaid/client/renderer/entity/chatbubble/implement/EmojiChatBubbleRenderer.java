package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.EntityGraphics;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.GifTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.listener.EmojiReloadListener;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class EmojiChatBubbleRenderer implements IChatBubbleRenderer {
    private final int width;
    private final int height;
    private final ResourceLocation bg;
    private final ResourceLocation emoji;

    public EmojiChatBubbleRenderer(ResourceLocation bg) {
        this.bg = bg;
        var randomEmojis = EmojiReloadListener.getRandomEmojis();
        if (randomEmojis.isPresent()) {
            var emojiRes = randomEmojis.get();
            this.emoji = emojiRes.location();
            this.width = emojiRes.width();
            this.height = emojiRes.height();
            // 如果是 gif 表情的话，需要手动注册
            if (emojiRes.isGif()) {
                this.registerGifImage();
            }
        } else {
            // 如果没有表情资源，就使用一个默认的空白资源
            this.emoji = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/chat_bubble/maid_emoji/emoji_0.png");
            this.width = 24;
            this.height = 24;
        }
    }

    private void registerGifImage() {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        if (manager.byPath.containsKey(this.emoji)) {
            return;
        }
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> manager.register(this.emoji, new GifTexture(this.emoji)));
        } else {
            manager.register(this.emoji, new GifTexture(this.emoji));
        }
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void render(EntityMaidRenderer renderer, EntityGraphics graphics) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.blit(this.emoji, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.bg;
    }
}
