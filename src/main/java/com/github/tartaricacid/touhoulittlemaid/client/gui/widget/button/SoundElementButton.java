package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

public class SoundElementButton extends FlatColorButton {
    private final ResourceLocation soundEvent;
    private final int soundCount;
    private final boolean otherColor;

    public SoundElementButton(int pX, int pY, int pWidth, int pHeight, ResourceLocation soundEvent, List<SoundBuffer> sounds, boolean otherColor, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, Component.translatable(soundEvent.toLanguageKey("button")), pOnPress);
        this.soundEvent = soundEvent;
        this.soundCount = sounds.size();
        this.otherColor = otherColor;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (otherColor) {
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x5f_9e9e9e, 0x5f_9e9e9e);
        } else {
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xff_434242, 0xff_434242);
        }
        if (this.isHoveredOrFocused()) {
            graphics.fillGradient(this.getX(), this.getY() + 1, this.getX() + 1, this.getY() + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            graphics.fillGradient(this.getX() + this.width - 1, this.getY() + 1, this.getX() + this.width, this.getY() + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            graphics.fillGradient(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        int i = getFGColor();
        this.renderString(graphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void renderString(GuiGraphics graphics, Font font, int pColor) {
        graphics.drawString(font, "▷", this.getX() + 5, this.getY() + (this.height - 8) / 2, 0xe0e0e0);
        graphics.drawString(font, this.getMessage(), this.getX() + 15, this.getY() + (this.height - 8) / 2, 0xfafafa);
        String countText = soundCount + "♫";
        int countTextWidth = font.width(countText);
        graphics.drawString(font, countText, this.getX() + this.getWidth() - countTextWidth - 5, this.getY() + (this.height - 8) / 2, 0xCCCCCC);
    }

    public ResourceLocation getSoundEvent() {
        return soundEvent;
    }
}
