package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * 用来渲染Tooltip,防止某些情况下tooltip被覆盖
 */
public interface ITooltipBtn {
    boolean isHovered();
    void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY);
}
