package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModelDetailsButton extends TouhouStateSwitchButton {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");
    private final Consumer<Boolean> onClick;
    private final MutableComponent name;

    public ModelDetailsButton(int xIn, int yIn, String langKey, Consumer<Boolean> onClick) {
        super(xIn, yIn, 128, 12, false);
        this.name = Component.translatable(langKey);
        this.onClick = onClick;
        this.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(Minecraft.getInstance().font, name, this.getX() + 14, this.getY() + 2, 0xffffffff, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        onClick.accept(this.isStateTriggered);
    }
}
