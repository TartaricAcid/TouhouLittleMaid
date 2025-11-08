package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BaubleButton extends Button implements ITooltipButton {
    private static final ResourceLocation BAUBLE_BUTTON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/bauble_button.png");
    private final int vStart;
    private final Component tooltip;

    public BaubleButton(int x, int y, boolean isOpen, OnPress onPress) {
        super(x + 85, y + 97, 54, 63, Component.empty(), onPress, DEFAULT_NARRATION);
        this.vStart = isOpen ? this.getHeight() : 0;
        if (isOpen) {
            this.tooltip = Component.translatable("gui.touhou_little_maid.bauble_button.close.desc");
        } else {
            this.tooltip = Component.translatable("gui.touhou_little_maid.bauble_button.open.desc");
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderTexture(guiGraphics, BAUBLE_BUTTON, this.getX(), this.getY(),
                0, this.vStart, 0, this.getWidth(), this.getHeight(),
                256, 256);
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered();
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        graphics.renderTooltip(mc.font, this.tooltip, mouseX, mouseY);
    }

    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int uOffset,
                              int vOffset, int yDiff, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        int i = vOffset;
        if (!this.isActive()) {
            i = vOffset + yDiff * 2;
        } else if (this.isHoveredOrFocused()) {
            i = vOffset + yDiff;
        }

        RenderSystem.enableDepthTest();
        pGuiGraphics.blit(pTexture, pX, pY, (float) uOffset, (float) i, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }
}
