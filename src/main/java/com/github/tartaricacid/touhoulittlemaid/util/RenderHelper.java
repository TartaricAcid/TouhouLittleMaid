package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.util.math.vector.Matrix4f;
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
            poseStack.mulPoseMatrix((new Matrix4f()).rotation(camera.rotation()));
            poseStack.scale(-scale, -scale, scale);
            float fontX = center ? (float) (-font.width(text)) / 2.0F : 0.0F;
            font.drawInBatch(text, fontX, yOffset, color, false, poseStack.last().pose(), buffer, seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, 0xf000f0);
            poseStack.popPose();
            buffer.endBatch();
        }
    }
}
