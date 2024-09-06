package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

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

    public static void renderEntityInInventory(PoseStack poseStack, int pX, int pY, int blitOffset, int scale, Quaternion pose, @Nullable Quaternion cameraOrientation, LivingEntity entity) {
        poseStack.pushPose();
        poseStack.translate(pX, pY, blitOffset + 50);
        poseStack.mulPoseMatrix(Matrix4f.createScaleMatrix((float) scale, (float) scale, (float) (-scale)));
        poseStack.mulPose(pose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (cameraOrientation != null) {
            cameraOrientation.conj();
            entityrenderdispatcher.overrideCameraOrientation(cameraOrientation);
        }
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, bufferSource, 15728880));
        RenderSystem.disableDepthTest();
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        entityrenderdispatcher.setRenderShadow(true);
        poseStack.popPose();
        Lighting.setupFor3DItems();
    }

    /**
     * 修改自原版的 net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventoryRaw(int, int, int, float, float, net.minecraft.world.entity.LivingEntity)
     * 主要添加了 posZ 的偏移
     */
    public static void renderEntityInInventory(int posX, int posY, int posZ, int scale, float mouseX, float mouseY, LivingEntity entity) {
        float angleX = (float) Math.atan(mouseX / 40.0F);
        float angleY = (float) Math.atan(mouseY / 40.0F);

        PoseStack viewStack = RenderSystem.getModelViewStack();
        viewStack.pushPose();
        viewStack.translate(posX, posY, posZ + 1050);
        viewStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();

        PoseStack poseStack = new PoseStack();
        poseStack.translate(0, 0, 1000);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        Quaternion zDeg = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion xDeg = Vector3f.XP.rotationDegrees(angleY * 20.0F);
        zDeg.mul(xDeg);
        poseStack.mulPose(zDeg);

        float yBodyRot = entity.yBodyRot;
        float yRot = entity.getYRot();
        float xRot = entity.getXRot();
        float yHeadRotO = entity.yHeadRotO;
        float yHeadRot = entity.yHeadRot;

        entity.yBodyRot = 180.0F + angleX * 20.0F;
        entity.setYRot(180.0F + angleX * 40.0F);
        entity.setXRot(-angleY * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher renderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        xDeg.conj();
        renderDispatcher.overrideCameraOrientation(xDeg);
        renderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> renderDispatcher.render(entity, 0, 0, 0, 0.0F, 1.0F, poseStack, bufferSource, 0xF000F0));
        bufferSource.endBatch();
        renderDispatcher.setRenderShadow(true);

        entity.yBodyRot = yBodyRot;
        entity.setYRot(yRot);
        entity.setXRot(xRot);
        entity.yHeadRotO = yHeadRotO;
        entity.yHeadRot = yHeadRot;

        viewStack.popPose();

        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
}
