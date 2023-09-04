package com.github.tartaricacid.touhoulittlemaid.client.animation.script;


import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GlWrapper {
    private static PoseStack poseStack;

    public static void setPoseStack(PoseStack poseStackIn) {
        poseStack = poseStackIn;
    }

    public static void clearPoseStack() {
        poseStack = null;
    }

    public static void translate(double x, double y, double z) {
        poseStack.translate(x, y, z);
    }

    public static void rotate(double angle, double x, double y, double z) {
        Vector3f vector3f = new Vector3f(normalize(x), normalize(y), normalize(z));
        poseStack.mulPose(new Quaternionf().rotateAxis((float) angle, vector3f));
    }

    public static void scale(double x, double y, double z) {
        poseStack.scale((float) x, (float) y, (float) z);
    }

    public static void pushMatrix() {
        poseStack.pushPose();
    }

    public static void popMatrix() {
        poseStack.popPose();
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
