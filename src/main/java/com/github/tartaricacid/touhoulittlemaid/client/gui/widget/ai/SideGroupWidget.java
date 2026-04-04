package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

public class SideGroupWidget implements Renderable {
    private final Component text;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public SideGroupWidget(int x, int y, Component text) {
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 20;
        this.text = text;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(this.x, this.y, this.x + this.width, this.y + this.height, 0xbf_090909, 0xbf_090909);
        graphics.drawCenteredString(Minecraft.getInstance().font, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xF3EFE0);
    }
}
