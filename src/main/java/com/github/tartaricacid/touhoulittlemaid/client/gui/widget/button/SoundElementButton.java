package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.mojang.blaze3d.audio.SoundBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (otherColor) {
            fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, 0x5f_9e9e9e, 0x5f_9e9e9e);
        } else {
            fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff_434242, 0xff_434242);
        }
        if (this.isHoveredOrFocused()) {
            fillGradient(poseStack, this.x, this.y + 1, this.x + 1, this.y + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(poseStack, this.x + this.width - 1, this.y + 1, this.x + this.width, this.y + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(poseStack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        int i = getFGColor();
        this.renderString(poseStack, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void renderString(PoseStack poseStack, Font font, int pColor) {
        drawString(poseStack, font, "▷", this.x + 5, this.y + (this.height - 8) / 2, 0xe0e0e0);
        drawString(poseStack, font, this.getMessage(), this.x + 15, this.y + (this.height - 8) / 2, 0xfafafa);
        String countText = soundCount + "♫";
        int countTextWidth = font.width(countText);
        drawString(poseStack, font, countText, this.x + this.getWidth() - countTextWidth - 5, this.y + (this.height - 8) / 2, 0xCCCCCC);
    }

    public ResourceLocation getSoundEvent() {
        return soundEvent;
    }
}
