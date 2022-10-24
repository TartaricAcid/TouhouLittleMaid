package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class MaidTabButton extends Button {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private final int left;

    public MaidTabButton(int x, int y, int left, IPressable onPressIn) {
        super(x, y, 24, 26, StringTextComponent.EMPTY, onPressIn);
        this.left = left;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(SIDE);
        RenderSystem.enableDepthTest();
        if (!this.active) {
            blit(matrixStack, this.x, this.y, left, 21, this.width, this.height, 256, 256);
        }
        blit(matrixStack, this.x + 4, this.y + 6, left, 47, 16, 16, 256, 256);
    }
}
