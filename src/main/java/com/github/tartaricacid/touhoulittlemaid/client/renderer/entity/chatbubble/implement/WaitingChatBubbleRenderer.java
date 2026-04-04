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

import javax.annotation.Nullable;

public class WaitingChatBubbleRenderer implements IChatBubbleRenderer {
    private static final float SECONDARY_TEXT_SCALE = 0.5f;
    private static final int SECONDARY_TEXT_COLOR = 0x666666;

    private final Font font;
    private final int width;
    private final int height;
    private final ResourceLocation bg;
    private final Component text;
    private final @Nullable Component secondaryText;
    private final ResourceLocation icon;

    public WaitingChatBubbleRenderer(ResourceLocation bg, Component text, @Nullable Component secondaryText, ResourceLocation icon) {
        this.font = Minecraft.getInstance().font;
        this.bg = bg;
        this.text = text;
        this.secondaryText = secondaryText;
        this.icon = icon;

        int mainWidth = font.width(text);
        if (secondaryText == null) {
            this.width = mainWidth;
            this.height = 16;
        } else {
            int secondaryWidth = Math.round(font.width(secondaryText) * SECONDARY_TEXT_SCALE);
            this.width = Math.max(mainWidth, secondaryWidth);
            this.height = font.lineHeight + Math.round(font.lineHeight * SECONDARY_TEXT_SCALE) + 3;
        }
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width + 18;
    }

    @Override
    public void render(EntityMaidRenderer renderer, EntityGraphics graphics) {
        graphics.getPoseStack().pushPose();
        float time = (Util.getMillis() % 3600);
        graphics.getPoseStack().translate(8, this.height / 2f, 0);
        graphics.getPoseStack().mulPose(Axis.ZP.rotationDegrees(time));
        graphics.blit(this.icon, -8, -8, 0, 0, 16, 16, 16, 16);
        graphics.getPoseStack().popPose();

        if (this.secondaryText == null) {
            graphics.drawString(font, this.text, 18, (this.height - font.lineHeight) / 2 + 1, 0x000000, false);
        } else {
            graphics.drawString(font, this.text, 18, 2, 0x000000, false);

            graphics.getPoseStack().pushPose();
            graphics.getPoseStack().translate(18, font.lineHeight + 1, 0);
            graphics.getPoseStack().scale(SECONDARY_TEXT_SCALE, SECONDARY_TEXT_SCALE, 1);
            graphics.drawString(font, this.secondaryText, 0, 5, SECONDARY_TEXT_COLOR, false);
            graphics.getPoseStack().popPose();
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.bg;
    }
}
