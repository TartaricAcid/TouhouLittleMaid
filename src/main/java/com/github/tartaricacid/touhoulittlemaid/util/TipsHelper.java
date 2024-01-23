package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public final class TipsHelper {
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");

    public static void renderTips(PoseStack matrixStack, Button button, Component text) {
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 450);

        if (text.equals(Component.empty())) {
            return;
        }

        int xOffset = button.x + button.getWidth() - 8;
        int yOffset = button.y - 12;
        long number = (System.currentTimeMillis() / 400) % 2;
        if (number == 1) {
            yOffset += 1;
        }

        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> split = font.split(text, 124);
        int size = split.size();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BUTTON);
        RenderSystem.disableDepthTest();
        if (size == 1) {
            int textWidth = Mth.clamp(font.width(text) + 26, 40, 150);
            GuiComponent.blit(matrixStack, xOffset, yOffset, 450, 0, 128, 10, 20, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 128, 20, 20, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 128, last, 20, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 128, 10, 20, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 20, yOffset - 2, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, text, xOffset + 5, yOffset + 4, 0XFFFF55);
        }

        if (size == 2) {
            yOffset = yOffset - 10;
            int textWidth = font.width(split.get(0)) + 26;
            GuiComponent.blit(matrixStack, xOffset, yOffset, 450, 0, 149, 10, 30, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 149, 20, 30, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 149, last, 30, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 149, 10, 30, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 20, yOffset + 5, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55);
            font.draw(matrixStack, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55);
        }

        if (size >= 3) {
            yOffset = yOffset - 20;
            int textWidth = font.width(split.get(0)) + 26;
            GuiComponent.blit(matrixStack, xOffset, yOffset, 450, 0, 180, 10, 40, 256, 256);
            int times = (textWidth - 20) / 20;
            int startX = xOffset + 10;
            for (int i = 0; i < times; i++) {
                GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 180, 20, 40, 256, 256);
                startX = startX + 20;
            }
            int last = textWidth - times * 20 - 20;
            GuiComponent.blit(matrixStack, startX, yOffset, 450, 10, 180, last, 40, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 10, yOffset, 450, 30, 180, 10, 40, 256, 256);
            GuiComponent.blit(matrixStack, xOffset + textWidth - 20, yOffset + 10, 450, 42, 128, 16, 16, 256, 256);
            font.draw(matrixStack, split.get(0), xOffset + 5, yOffset + 4, 0XFFFF55);
            font.draw(matrixStack, split.get(1), xOffset + 5, yOffset + 14, 0XFFFF55);
            font.draw(matrixStack, split.get(2), xOffset + 5, yOffset + 24, 0XFFFF55);
        }

        matrixStack.popPose();
    }
}
