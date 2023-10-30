package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;


public class FlatColorButton extends Button {
    private boolean isSelect = false;
    private List<Component> tooltips;

    public FlatColorButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public FlatColorButton setTooltips(String key) {
        tooltips = Collections.singletonList(new TranslatableComponent(key));
        return this;
    }

    public FlatColorButton setTooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
        return this;
    }

    public void renderToolTip(PoseStack poseStack, Screen screen, int pMouseX, int pMouseY) {
        if (this.isHovered && tooltips != null) {
            screen.renderComponentTooltip(poseStack, tooltips, pMouseX, pMouseY);
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (isSelect) {
            fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff_1E90FF, 0xff_1E90FF);
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


    public void renderString(PoseStack poseStack, Font font, int pColor) {
        drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xF3EFE0);
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
