package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public final class TipsHelper {
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");

    public static void renderTips(MatrixStack matrixStack, Button button, ITextComponent text) {
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 450);

        if (text.equals(StringTextComponent.EMPTY)) {
            return;
        }

        int xOffset = button.x + button.getWidth() - 8;
        int yOffset = button.y - 12;
        long number = (System.currentTimeMillis() / 400) % 2;
        if (number == 1) {
            yOffset += 1;
        }

        FontRenderer font = Minecraft.getInstance().font;
        List<IReorderingProcessor> split = font.split(text, 124);
        int size = split.size();

        RenderSystem.disableDepthTest();
        Minecraft.getInstance().textureManager.bind(BUTTON);
        if (size == 1) {
            int textWidth = MathHelper.clamp(font.width(text) + 26, 40, 150);
            AbstractGui.blit(matrixStack, xOffset, yOffset, 450, 0, 128, 10, 20, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 128, 20, 20, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 128, last, 20, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 128, 10, 20, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 20, yOffset - 2, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, text, xOffset + 5, yOffset + 4, 0XFFFF55);
        }

        if (size == 2) {
            yOffset = yOffset - 10;
            int textWidth = font.width(split.get(0)) + 26;
            AbstractGui.blit(matrixStack, xOffset, yOffset, 450, 0, 149, 10, 30, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 149, 20, 30, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 149, last, 30, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 149, 10, 30, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 20, yOffset + 5, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55);
            font.draw(matrixStack, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55);
        }

        if (size >= 3) {
            yOffset = yOffset - 20;
            int textWidth = font.width(split.get(0)) + 26;
            AbstractGui.blit(matrixStack, xOffset, yOffset, 450, 0, 180, 10, 40, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 180, 20, 40, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            AbstractGui.blit(matrixStack, startX, yOffset, 450, 10, 180, last, 40, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 180, 10, 40, 256, 256);
            AbstractGui.blit(matrixStack, xOffset + textWidth - 20, yOffset + 10, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55);
            font.draw(matrixStack, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55);
            font.draw(matrixStack, split.get(2), xOffset + 5, yOffset + 24, 0XFFFF55);
        }

        matrixStack.popPose();
    }
}
