package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
            poseStack.mulPoseMatrix(new Matrix4f(camera.rotation()));
            poseStack.scale(-scale, -scale, scale);
            float fontX = center ? (float) (-font.width(text)) / 2.0F : 0.0F;
            font.drawInBatch(text, fontX, yOffset, color, false, poseStack.last().pose(), buffer, seeThrough, 0, 0xf000f0);
            poseStack.popPose();
            buffer.endBatch();
        }
    }

    public static void renderLine(PoseStack poseStack, VertexConsumer consumer, Vec3 start, Vec3 end, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();

        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
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
            consumer.vertex(matrix4f, (float) x1, (float) y, (float) z1).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            consumer.vertex(matrix4f, (float) x2, (float) y, (float) z2).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();

            consumer.vertex(matrix4f, (float) x1, (float) y, (float) z1).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(matrix4f, (float) x2, (float) y, (float) z2).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

            consumer.vertex(matrix4f, (float) x1, (float) y, (float) z1).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            consumer.vertex(matrix4f, (float) x2, (float) y, (float) z2).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        }
    }

    public static void addChainedFilledBoxVertices(PoseStack matrixStack, AABB aabb, float pRed, float pGreen, float pBlue, float pAlpha) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        float pMinX = (float) aabb.minX;
        float pMinY = (float) aabb.minY;
        float pMinZ = (float) aabb.minZ;
        float pMaxX = (float) aabb.maxX;
        float pMaxY = (float) aabb.maxY;
        float pMaxZ = (float) aabb.maxZ;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.last().pose();

        builder.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        builder.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();

        tesselator.end();
        RenderSystem.enableTexture();
    }
}
