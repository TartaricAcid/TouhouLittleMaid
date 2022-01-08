package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;

public class WirelessIOButton extends ToggleWidget {
    protected final WirelessIOButton.ITooltip onTooltip;
    private final BiConsumer<Double, Double> onClick;

    public WirelessIOButton(int xIn, int yIn, int widthIn, int heightIn, boolean triggered, BiConsumer<Double, Double> onClick, WirelessIOButton.ITooltip onTooltip) {
        super(xIn, yIn, widthIn, heightIn, triggered);
        this.onClick = onClick;
        this.onTooltip = onTooltip;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        onClick.accept(mouseX, mouseY);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if (this.isHovered()) {
            this.onTooltip.onTooltip(matrixStack, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface ITooltip {
        void onTooltip(MatrixStack matrixStack, int x, int y);
    }
}
