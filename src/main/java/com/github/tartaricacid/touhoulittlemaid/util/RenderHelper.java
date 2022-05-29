package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.math.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.GuiUtils;

@OnlyIn(Dist.CLIENT)
public final class RenderHelper {
    public static void renderBackground(Matrix4f mat, int x, int y, int width, int height) {
        x = x - width;
        y = y - height;
        int backgroundColor = 0xF0100010;
        int borderColorStart = 0x505000FF;
        int borderColorEnd = 0x5028007f;
        GuiUtils.drawGradientRect(mat, 300, x - 3, y - 4, x + width + 3, y - 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, 300, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, 300, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, 300, x - 4, y - 3, x - 3, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, 300, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, 300, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(mat, 300, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(mat, 300, x - 3, y - 3, x + width + 3, y - 3 + 1, borderColorStart, borderColorStart);
        GuiUtils.drawGradientRect(mat, 300, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColorEnd, borderColorEnd);
    }
}
