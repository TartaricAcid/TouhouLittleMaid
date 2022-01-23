package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class ShapeDraw {
    public static void drawSector(int x, int y, int r, double startAngle, double endAngle, int precision, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(x, y, 0).color(red, green, blue, alpha).endVertex();
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = (int) (endAngle / precisionAngle); i >= (int) (startAngle / precisionAngle); i--) {
            bufferbuilder.vertex(x + r * Math.cos(i * precisionAngle),
                    y + r * Math.sin(i * precisionAngle),
                    0).color(red, green, blue, alpha).endVertex();
        }
        tessellator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawCircle(int x, int y, int r, int precision, int color) {
        drawSector(x, y, r, 0, 2 * Math.PI, precision, color);
    }
}
