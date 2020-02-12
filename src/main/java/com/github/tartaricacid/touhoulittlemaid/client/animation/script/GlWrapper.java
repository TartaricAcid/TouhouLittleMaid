package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import org.lwjgl.opengl.GL11;

public class GlWrapper {
    public static void translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        GL11.glScalef(x, y, z);
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }
}
