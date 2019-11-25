package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * @author TartaricAcid
 * @date 2019/8/30 15:31
 **/
@SideOnly(Side.CLIENT)
public final class RenderHelper {
    public static int SPHERE_OUT_INDEX;
    public static int SPHERE_IN_INDEX;
    public static int CYLINDER_OUT_INDEX;
    public static int CYLINDER_IN_INDEX;

    static {
        genSphere();
        genCylinder();
    }

    private RenderHelper() {
    }

    private static void genSphere() {
        Sphere sphere = new Sphere();

        sphere.setDrawStyle(GLU.GLU_FILL);
        sphere.setNormals(GLU.GLU_SMOOTH);
        sphere.setOrientation(GLU.GLU_OUTSIDE);

        SPHERE_OUT_INDEX = GlStateManager.glGenLists(1);
        GlStateManager.glNewList(SPHERE_OUT_INDEX, GL11.GL_COMPILE);
        sphere.draw(1, 10, 10);
        GlStateManager.glEndList();

        sphere.setOrientation(GLU.GLU_INSIDE);

        SPHERE_IN_INDEX = GlStateManager.glGenLists(1);
        GlStateManager.glNewList(SPHERE_IN_INDEX, GL11.GL_COMPILE);
        sphere.draw(1, 10, 10);
        GlStateManager.glEndList();
    }

    private static void genCylinder() {
        Cylinder cylinder = new Cylinder();

        cylinder.setDrawStyle(GLU.GLU_FILL);
        cylinder.setNormals(GLU.GLU_SMOOTH);
        cylinder.setOrientation(GLU.GLU_OUTSIDE);

        CYLINDER_OUT_INDEX = GlStateManager.glGenLists(1);
        GlStateManager.glNewList(CYLINDER_OUT_INDEX, GL11.GL_COMPILE);
        cylinder.draw(1, 1, 1, 16, 16);
        GlStateManager.glEndList();

        cylinder.setOrientation(GLU.GLU_INSIDE);

        CYLINDER_IN_INDEX = GlStateManager.glGenLists(1);
        GlStateManager.glNewList(CYLINDER_IN_INDEX, GL11.GL_COMPILE);
        cylinder.draw(1, 1, 1, 16, 16);
        GlStateManager.glEndList();
    }

    public static void drawSector(int x, int y, int r, double startAngle, double endAngle, int precision, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0).color(red, green, blue, alpha).endVertex();
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = (int) (endAngle / precisionAngle); i >= (int) (startAngle / precisionAngle); i--) {
            bufferbuilder.pos(x + r * Math.cos(i * precisionAngle),
                    y + r * Math.sin(i * precisionAngle),
                    0).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(int x, int y, int r, int precision, int color) {
        drawSector(x, y, r, 0, 2 * Math.PI, precision, color);
    }
}
