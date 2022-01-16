package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class ModelDetailsButton extends ToggleWidget {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");
    private final Consumer<Boolean> onClick;
    private final TranslationTextComponent name;

    public ModelDetailsButton(int xIn, int yIn, String langKey, Consumer<Boolean> onClick) {
        super(xIn, yIn, 128, 12, false);
        this.name = new TranslationTextComponent(langKey);
        this.onClick = onClick;
        this.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        drawString(matrixStack, Minecraft.getInstance().font, name, this.x + 14, this.y + 2, 0xffffffff);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        onClick.accept(this.isStateTriggered);
    }
}
