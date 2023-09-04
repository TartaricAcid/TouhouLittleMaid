package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MaidTabButton extends Button {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private final int left;

    public MaidTabButton(int x, int y, int left, Button.OnPress onPressIn) {
        super(Button.builder(Component.empty(), onPressIn).pos(x, y).size(24, 26));
        this.left = left;
    }


    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        if (!this.active) {
            graphics.blit(SIDE, this.getX(), this.getY(), left, 21, this.width, this.height, 256, 256);
        }
        graphics.blit(SIDE, this.getX() + 4, this.getY() + 6, left, 47, 16, 16, 256, 256);
    }
}