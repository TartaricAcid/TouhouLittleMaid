package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;


public class SideButton extends Button {
    protected final Type type;

    protected boolean isSelect = false;

    public SideButton(Type type, int pX, int pY, Component pMessage, OnPress pOnPress) {
        super(pX, pY, 100, 20, pMessage, pOnPress, DEFAULT_NARRATION);
        this.type = type;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width,
                this.getY() + this.height, 0xbf_090909, 0xbf_090909);
        if (isSelect) {
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + 2,
                    this.getY() + this.height, 0xff_55ff55, 0xff_55ff55);
        }
        if (this.isHoveredOrFocused() || isSelect) {
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width,
                    this.getY() + this.height, 0x2f_F3EFE0, 0x2f_F3EFE0);
        }
        this.renderString(graphics, Minecraft.getInstance().font, 0xF3EFE0);
    }

    @Override
    public void renderString(GuiGraphics graphics, Font font, int pColor) {
        graphics.drawCenteredString(font, this.getMessage(), this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2, 0xF3EFE0);
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void updateSelect(Type currentType) {
        this.setSelect(this.type == currentType);
        this.active = (this.type != currentType);
    }

    public Type getType() {
        return type;
    }
}
