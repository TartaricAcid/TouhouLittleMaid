package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MaidAIChatConfigButton extends Button {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_ai_chat_config.png");
    private final MaidAIChatConfigButton.OnPress leftPress;
    private final MaidAIChatConfigButton.OnPress rightPress;
    private boolean leftClicked = false;
    private Component value;

    public MaidAIChatConfigButton(int x, int y, Component title, Component value, MaidAIChatConfigButton.OnPress onLeftPressIn, MaidAIChatConfigButton.OnPress onRightPressIn) {
        super(Button.builder(title, b -> {
        }).pos(x, y).size(164, 13));
        this.leftPress = onLeftPressIn;
        this.rightPress = onRightPressIn;
        this.value = value;
    }

    public MaidAIChatConfigButton(int x, int y, Component title, Component value, MaidAIChatConfigButton.OnPress onPress) {
        this(x, y, title, value, onPress, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        if (this.isHovered) {
            graphics.blit(ICON, this.getX(), this.getY(), 6, 150, this.width, this.height, 256, 256);
        } else {
            graphics.blit(ICON, this.getX(), this.getY(), 6, 137, this.width, this.height, 256, 256);
        }
        drawButtonText(graphics, mc.font);
    }

    public void setValue(Component value) {
        this.value = value;
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        if (!this.active || !this.visible) {
            return false;
        }
        boolean leftClickX = (this.getX() + 62) <= mouseX && mouseX <= (this.getX() + 72);
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

    public void drawButtonText(GuiGraphics graphics, Font font) {
        float scale = 0.75f;

        FormattedCharSequence leftText = this.getMessage().getVisualOrderText();
        FormattedCharSequence rightText = this.value.getVisualOrderText();

        float leftTextX = (this.getX() + 5) / scale;
        float leftTextY = (this.getY() + 4) / scale;
        float rightTextX = (this.getX() + 113 - font.width(rightText) * scale / 2f) / scale;
        float rightTextY = (this.getY() + 4) / scale;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1);
        graphics.drawString(font, leftText, leftTextX, leftTextY, 0x444444, false);
        graphics.drawString(font, rightText, rightTextX, rightTextY, 0x55ff55, false);
        graphics.pose().popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(MaidAIChatConfigButton button);
    }
}
