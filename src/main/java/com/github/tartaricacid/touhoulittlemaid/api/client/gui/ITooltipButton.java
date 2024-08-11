package com.github.tartaricacid.touhoulittlemaid.api.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

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
    void renderTooltip(PoseStack poseStack, Minecraft mc, int mouseX, int mouseY);
}
