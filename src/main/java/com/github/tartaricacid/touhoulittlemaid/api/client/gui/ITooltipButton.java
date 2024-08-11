package com.github.tartaricacid.touhoulittlemaid.api.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * 女仆主界面用，按钮用于渲染文本提示
 */
public interface ITooltipButton {
    /**
     * 鼠标是否悬浮其上
     */
    boolean isTooltipHovered();

    /**
     * 渲染文本提示
     */
    void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY);
}
