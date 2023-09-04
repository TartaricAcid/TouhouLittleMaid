package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

public final class ShapeDraw {
    public static void drawSector(int x, int y, int r, double startAngle, double endAngle, int precision, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(x, y, 0).color(red, green, blue, alpha).endVertex();
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = (int) (endAngle / precisionAngle); i >= (int) (startAngle / precisionAngle); i--) {
            bufferbuilder.vertex(x + r * Math.cos(i * precisionAngle),
                    y + r * Math.sin(i * precisionAngle),
                    0).color(red, green, blue, alpha).endVertex();
        }
        tesselator.end();
        RenderSystem.disableBlend();
    }

    public static void drawCircle(int x, int y, int r, int precision, int color) {
        drawSector(x, y, r, 0, 2 * Math.PI, precision, color);
    }
}
