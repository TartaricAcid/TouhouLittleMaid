package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public final class TipsHelper {
    private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");

    public static void renderTips(GuiGraphics graphics, Button button, Component text) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 450);

        if (text.equals(Component.empty())) {
            return;
        }

        int xOffset = button.getX() + button.getWidth() - 8;
        int yOffset = button.getY() - 12;
        long number = (System.currentTimeMillis() / 400) % 2;
        if (number == 1) {
            yOffset += 1;
        }

        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> split = font.split(text, 124);
        int size = split.size();

        if (size == 1) {
            int textWidth = Mth.clamp(font.width(text) + 26, 40, 150);
            RenderSystem.enableDepthTest();
            graphics.blit(BUTTON, xOffset, yOffset, 0, 128, 10, 20);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                graphics.blit(BUTTON, startX, yOffset, 10, 128, 20, 20);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            graphics.blit(BUTTON, startX, yOffset, 10, 128, last, 20);
            graphics.blit(BUTTON, xOffset + textWidth - 10, yOffset, 30, 128, 10, 20);
            graphics.blit(BUTTON, xOffset + textWidth - 20, yOffset - 2, 42, 128, 16, 16);
            graphics.drawString(font, text, xOffset + 5, yOffset + 4, 0XFFFF55, false);
        }

        if (size == 2) {
            yOffset = yOffset - 10;
            int textWidth = font.width(split.get(0)) + 26;
            RenderSystem.enableDepthTest();
            graphics.blit(BUTTON, xOffset, yOffset, 0, 149, 10, 30);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                graphics.blit(BUTTON, startX, yOffset, 10, 149, 20, 30);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            graphics.blit(BUTTON, startX, yOffset, 10, 149, last, 30);
            graphics.blit(BUTTON, xOffset + textWidth - 10, yOffset, 30, 149, 10, 30);
            graphics.blit(BUTTON, xOffset + textWidth - 20, yOffset + 5, 42, 128, 16, 16);
            graphics.drawString(font, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55, false);
            graphics.drawString(font, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55, false);
        }

        if (size >= 3) {
            yOffset = yOffset - 20;
            int textWidth = font.width(split.get(0)) + 26;
            RenderSystem.enableDepthTest();
            graphics.blit(BUTTON, xOffset, yOffset, 0, 180, 10, 40);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                graphics.blit(BUTTON, startX, yOffset, 10, 180, 20, 40);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            graphics.blit(BUTTON, startX, yOffset, 10, 180, last, 40);
            graphics.blit(BUTTON, xOffset + textWidth - 10, yOffset, 30, 180, 10, 40);
            graphics.blit(BUTTON, xOffset + textWidth - 20, yOffset + 10, 42, 128, 16, 16);
            graphics.drawString(font, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55, false);
            graphics.drawString(font, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55, false);
            graphics.drawString(font, split.get(2), xOffset + 5, yOffset + 24, 0XFFFF55, false);
        }

        graphics.pose().popPose();
    }
}
