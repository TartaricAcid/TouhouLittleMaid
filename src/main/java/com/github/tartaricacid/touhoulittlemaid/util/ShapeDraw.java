package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

public final class ShapeDraw {
    public static void drawSector(int x, int y, int r, double startAngle, double endAngle, int precision, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.addVertex(x, y, 0).setColor(red, green, blue, alpha);
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = (int) (endAngle / precisionAngle); i >= (int) (startAngle / precisionAngle); i--) {
            bufferbuilder.addVertex((float) (x + r * Math.cos(i * precisionAngle)),
                    (float) (y + r * Math.sin(i * precisionAngle)),
                    0).setColor(red, green, blue, alpha);
        }
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    public static void drawCircle(int x, int y, int r, int precision, int color) {
        drawSector(x, y, r, 0, 2 * Math.PI, precision, color);
    }
}
