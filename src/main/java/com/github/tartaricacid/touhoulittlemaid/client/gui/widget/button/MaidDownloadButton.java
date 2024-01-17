package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.ModelDownloadGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class MaidDownloadButton extends ImageButton {
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");

    public MaidDownloadButton(int pX, int pY, ResourceLocation texture) {
        super(pX, pY, 41, 20, 0, 86, 20, texture, (b) -> {
            InfoGetManager.STATUE = InfoGetManager.Statue.NOT_UPDATE;
            Minecraft.getInstance().setScreen(new ModelDownloadGui());
        });
    }

    public void renderExtraTips(GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 450);

        MutableComponent text = getText();
        if (text.equals(Component.empty())) {
            return;
        }

        int xOffset = this.getX() + this.width / 2;
        int yOffset = this.getY() - 22;
        long number = (System.currentTimeMillis() / 400) % 2;
        if (number == 1) {
            yOffset += 1;
        }

        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> split = font.split(text, 150);
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
            if (!text.equals(Component.empty())) {
                graphics.drawString(font, text, xOffset + 5, yOffset + 4, ChatFormatting.YELLOW.getColor(), false);
            }
        }

        if (size >= 2) {
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
            graphics.drawString(font, split.get(0), xOffset + 5, yOffset + 4, ChatFormatting.YELLOW.getColor(), false);
            graphics.drawString(font, split.get(1), xOffset + 5, yOffset + 14, ChatFormatting.YELLOW.getColor(), false);
        }

        graphics.pose().popPose();
    }

    private MutableComponent getText() {
        if (InfoGetManager.STATUE == InfoGetManager.Statue.FIRST) {
            return Component.translatable("gui.touhou_little_maid.button.model_download.statue.first");
        }
        if (InfoGetManager.STATUE == InfoGetManager.Statue.UPDATE) {
            return Component.translatable("gui.touhou_little_maid.button.model_download.statue.update");
        }
        return Component.empty();
    }
}
