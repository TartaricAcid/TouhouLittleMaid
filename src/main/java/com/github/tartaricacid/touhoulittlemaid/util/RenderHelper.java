package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class RenderHelper {
    public static void renderFloatingText(MatrixStack poseStack, String text, Vector3d vec, int color, float scale, float yOffset) {
        renderFloatingText(poseStack, text, vec.x + 0.5, vec.y + 1.5, vec.z + 0.5, color, scale, true, yOffset, true);
    }

    public static void renderFloatingText(MatrixStack poseStack, String text, double x, double y, double z, int color, float scale, boolean center, float yOffset, boolean seeThrough) {
        Minecraft minecraft = Minecraft.getInstance();
        IRenderTypeBuffer.Impl buffer = minecraft.renderBuffers().bufferSource();
        ActiveRenderInfo camera = minecraft.gameRenderer.getMainCamera();
        if (camera.isInitialized()) {
            FontRenderer font = minecraft.font;
            double viewX = camera.getPosition().x;
            double viewY = camera.getPosition().y;
            double viewZ = camera.getPosition().z;
            poseStack.pushPose();
            poseStack.translate((float) (x - viewX), (float) (y - viewY) + 0.07F, (float) (z - viewZ));
            poseStack.mulPose(camera.rotation());
            poseStack.scale(-scale, -scale, scale);
            float fontX = center ? (float) (-font.width(text)) / 2.0F : 0.0F;
            font.drawInBatch(text, fontX, yOffset, color, false, poseStack.last().pose(), buffer, seeThrough, 0, 0xf000f0);
            poseStack.popPose();
            buffer.endBatch();
        }
    }

    public static void renderLine(MatrixStack poseStack, IVertexBuilder consumer, Vector3d start, Vector3d end, float red, float green, float blue) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();

        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z).color(red, green, blue, 1.0F).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    }

    public static void renderCylinder(MatrixStack poseStack, IVertexBuilder consumer, Vector3d centerPos, double radius, int precision, float red, float green, float blue) {
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

    public static void addChainedFilledBoxVertices(MatrixStack matrixStack, IRenderTypeBuffer.Impl buffer, AxisAlignedBB aabb, float pRed, float pGreen, float pBlue, float pAlpha) {
        float pMinX = (float) aabb.minX;
        float pMinY = (float) aabb.minY;
        float pMinZ = (float) aabb.minZ;
        float pMaxX = (float) aabb.maxX;
        float pMaxY = (float) aabb.maxY;
        float pMaxZ = (float) aabb.maxZ;
        IVertexBuilder pConsumer = buffer.getBuffer(RenderType.LINES);
        Matrix4f matrix4f = matrixStack.last().pose();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMinY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMinX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
    }
}
