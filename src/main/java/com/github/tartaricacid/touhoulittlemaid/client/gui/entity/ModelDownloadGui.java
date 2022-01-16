package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class ModelDownloadGui extends Screen {
    protected ModelDownloadGui() {
        super(new StringTextComponent("Model Pack Download GUI"));
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
