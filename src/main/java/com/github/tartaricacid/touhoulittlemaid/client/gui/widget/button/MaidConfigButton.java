package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MaidConfigButton extends Button {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private final MaidConfigButton.OnPress leftPress;
    private final MaidConfigButton.OnPress rightPress;
    private boolean leftClicked = false;
    private Component value;

    public MaidConfigButton(int x, int y, Component title, Component value, MaidConfigButton.OnPress onLeftPressIn, MaidConfigButton.OnPress onRightPressIn) {
        super(Button.builder(title, b -> {
        }).pos(x, y).size(164, 13));
        this.leftPress = onLeftPressIn;
        this.rightPress = onRightPressIn;
        this.value = value;
    }

    public MaidConfigButton(int x, int y, Component title, Component value, MaidConfigButton.OnPress onPress) {
        this(x, y, title, value, onPress, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        if (this.isHovered) {
            graphics.blit(ICON, this.getX(), this.getY(), 63, 141, this.width, this.height, 256, 256);
        } else {
            graphics.blit(ICON, this.getX(), this.getY(), 63, 128, this.width, this.height, 256, 256);
        }
        graphics.drawString(mc.font, this.getMessage(), this.getX() + 5, this.getY() + 3, 0x444444, false);
        drawCenteredStringWithoutShadow(graphics, mc.font, this.value, this.getX() + 142, this.getY() + 3, ChatFormatting.GREEN.getColor());
    }

    public void setValue(Component value) {
        this.value = value;
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        if (!this.active || !this.visible) {
            return false;
        }
        boolean leftClickX = (this.getX() + 120) <= mouseX && mouseX <= (this.getX() + 130);
        boolean rightClickX = (this.getX() + 154) <= mouseX && mouseX <= (this.getX() + 164);
        boolean clickY = this.getY() <= mouseY && mouseY <= (this.getY() + this.getHeight());
        if (leftClickX && clickY) {
            leftClicked = true;
            return true;
        }
        if (rightClickX && clickY) {
            leftClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public void onPress() {
        if (leftClicked) {
            leftPress.onPress(this);
        } else {
            rightPress.onPress(this);
        }
    }

    public void drawCenteredStringWithoutShadow(GuiGraphics graphics, Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        graphics.drawString(pFont, formattedcharsequence, pX - pFont.width(formattedcharsequence) / 2, pY, pColor, false);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(MaidConfigButton button);
    }
}
