package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;

public class GlWrapper {
    private static MatrixStack matrixStack;

    public static void setMatrixStack(MatrixStack matrixStackIn) {
        matrixStack = matrixStackIn;
    }

    public static void clearMatrixStack() {
        matrixStack = null;
    }

    public static void translate(double x, double y, double z) {
        matrixStack.translate(x, y, z);
    }

    public static void rotate(double angle, double x, double y, double z) {
        Vector3f vector3f = new Vector3f(normalize(x), normalize(y), normalize(z));
        matrixStack.mulPose(vector3f.rotationDegrees((float) angle));
    }

    public static void scale(double x, double y, double z) {
        matrixStack.scale((float) x, (float) y, (float) z);
    }

    public static void pushMatrix() {
        matrixStack.pushPose();
    }

    public static void popMatrix() {
        matrixStack.popPose();
    }

    private static float normalize(double value) {
        if (value >= 1.0E-5) {
            return 1.0f;
        } else if (value <= -1.0E-5) {
            return -1.0f;
        } else {
            return 0;
        }
    }
}
