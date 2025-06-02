package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.implement;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.EntityGraphics;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class ProgressChatBubbleRenderer implements IChatBubbleRenderer {
    private static final int MAX_WIDTH = 240;

    private final Font font;
    private final ResourceLocation bg;
    private final List<FormattedCharSequence> split;
    private final int barBackgroundColor;
    private final int barForegroundColor;
    private final double progress;
    private final boolean alignCenter;
    private final int width;
    private final int height;

    public ProgressChatBubbleRenderer(ResourceLocation bg, Component text, int barBackgroundColor, int barForegroundColor,
                                      double progress, boolean alignCenter) {
        this.font = Minecraft.getInstance().font;
        this.bg = bg;
        this.split = font.split(text, MAX_WIDTH);
        this.barBackgroundColor = barBackgroundColor;
        this.barForegroundColor = barForegroundColor;
        this.progress = Mth.clamp(progress, 0d, 1d);
        this.alignCenter = alignCenter;
        this.width = Mth.clamp(font.width(text), 100, MAX_WIDTH);
        this.height = split.size() * font.lineHeight + 12;
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
        int y = 0;
        for (FormattedCharSequence sequence : this.split) {
            if (this.alignCenter) {
                int distance = this.width - font.width(sequence);
                graphics.drawString(font, sequence, distance / 2, y, 0x000000, false);
            } else {
                graphics.drawString(font, sequence, 0, y, 0x000000, false);
            }
            y += font.lineHeight;
        }
        y += 2;
        int margin = 1;
        int barHeight = 10;
        graphics.fill(0, y, this.width, y + barHeight, this.barBackgroundColor);
        if (this.progress > 0) {
            int barWidth = (int) ((this.width - 2 * margin) * this.progress);
            graphics.pose().translate(0, 0, -0.01);
            graphics.fill(margin, y + margin, barWidth, y + barHeight - margin, this.barForegroundColor);
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.bg;
    }
}
