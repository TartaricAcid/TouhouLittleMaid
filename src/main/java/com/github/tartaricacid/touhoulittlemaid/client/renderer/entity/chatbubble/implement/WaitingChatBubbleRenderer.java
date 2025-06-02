package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.EntityGraphics;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WaitingChatBubbleRenderer implements IChatBubbleRenderer {
    private final Font font;
    private final int width;
    private final ResourceLocation bg;
    private final Component text;
    private final ResourceLocation icon;

    public WaitingChatBubbleRenderer(ResourceLocation bg, Component text, ResourceLocation icon) {
        this.font = Minecraft.getInstance().font;
        this.width = font.width(text);
        this.bg = bg;
        this.text = text;
        this.icon = icon;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getWidth() {
        return this.width + 18;
    }

    @Override
    public void render(EntityMaidRenderer renderer, EntityGraphics graphics) {
        graphics.pose().pushPose();
        float time = (Util.getMillis() % 3600);
        graphics.pose().translate(8, 8, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(time));
        graphics.blit(this.icon, -8, -8, 0, 0, 16, 16, 16, 16);
        graphics.pose().popPose();
        graphics.drawString(font, this.text, 18, (16 - font.lineHeight) / 2 + 1, 0x000000, false);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.bg;
    }
}
