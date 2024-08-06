package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class RenderHelper {
    public static void renderFloatingText(PoseStack poseStack, String text, Vec3 vec, int color, float scale, float yOffset) {
        renderFloatingText(poseStack, text, vec.x + 0.5, vec.y + 1.5, vec.z + 0.5, color, scale, true, yOffset, true);
    }

    public static void renderFloatingText(PoseStack poseStack, String text, double x, double y, double z, int color, float scale, boolean center, float yOffset, boolean seeThrough) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        if (camera.isInitialized()) {
            Font font = minecraft.font;
            double viewX = camera.getPosition().x;
            double viewY = camera.getPosition().y;
            double viewZ = camera.getPosition().z;
            poseStack.pushPose();
            poseStack.translate((float) (x - viewX), (float) (y - viewY) + 0.07F, (float) (z - viewZ));
            poseStack.mulPose((new Matrix4f()).rotation(camera.rotation()));
            poseStack.scale(-scale, -scale, scale);
            float fontX = center ? (float) (-font.width(text)) / 2.0F : 0.0F;
            font.drawInBatch(text, fontX, yOffset, color, false, poseStack.last().pose(), buffer, seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, 0xf000f0);
            poseStack.popPose();
            buffer.endBatch();
        }
    }

    public static void renderLine(PoseStack poseStack, VertexConsumer consumer, Vec3 start, Vec3 end, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f,1.0F, 0.0F, 0.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f,1.0F, 0.0F, 0.0F);

        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f,0.0F, 1.0F, 0.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f,0.0F, 1.0F, 0.0F);

        addVertex(consumer, matrix4f, (float) start.x, (float) start.y, (float) start.z, red, green, blue, 1.0F, matrix3f,0.0F, 0.0F, 1.0F);
        addVertex(consumer, matrix4f, (float) end.x, (float) end.y, (float) end.z, red, green, blue, 1.0F, matrix3f,0.0F, 0.0F, 1.0F);
    }

    public static void renderCylinder(PoseStack poseStack, VertexConsumer consumer, Vec3 centerPos, double radius, int precision, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        double precisionAngle = 2 * Math.PI / precision;
        for (int i = precision; i >= 0; i--) {
            double x1 = centerPos.x + radius * Math.cos(i * precisionAngle);
            double x2 = centerPos.x + radius * Math.cos((i - 1) * precisionAngle);
            double y = centerPos.y;
            double z1 = centerPos.z + radius * Math.sin(i * precisionAngle);
            double z2 = centerPos.z + radius * Math.sin((i - 1) * precisionAngle);
            addVertex(consumer,matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f,1.0F, 0.0F, 0.0F);
            addVertex(consumer,matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f,1.0F, 0.0F, 0.0F);

            addVertex(consumer,matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f,0.0F, 1.0F, 0.0F);
            addVertex(consumer,matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f,0.0F, 1.0F, 0.0F);

            addVertex(consumer,matrix4f, (float) x1, (float) y, (float) z1, red, green, blue, 1.0F, matrix3f,0.0F, 0.0F, 1.0F);
            addVertex(consumer,matrix4f, (float) x2, (float) y, (float) z2, red, green, blue, 1.0F, matrix3f,0.0F, 0.0F, 1.0F);
        }
    }

    public static void addVertex(VertexConsumer consumer ,Matrix4f matrix4f, float x, float y, float z, float red, float green, float blue, float alpha, Matrix3f matrix3f, float normalX, float normalY, float normalZ) {
        Vector3f vector3f = matrix3f.transform(new Vector3f(normalX,normalY,normalZ));
        consumer.addVertex(matrix4f, x, y, z).setColor(red, green, blue, alpha).setNormal(vector3f.x(), vector3f.y(), vector3f.z());
    }
}
