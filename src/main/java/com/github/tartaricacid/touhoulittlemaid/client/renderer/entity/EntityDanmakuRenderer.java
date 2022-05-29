package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EntityDanmakuRenderer extends EntityRenderer<EntityDanmaku> {
    private static final ResourceLocation DANMAKU_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/danmaku.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(DANMAKU_TEXTURE);

    public EntityDanmakuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    private static void vertex(VertexConsumer bufferIn, Matrix4f pose, Matrix3f normal, double x, double y, double texU, double texV, int packedLight) {
        bufferIn.vertex(pose, (float) x, (float) y, 0.0F).color(255, 255, 255, 255).uv((float) texU, (float) texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public void render(EntityDanmaku entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        // 获取相关数据
        DanmakuColor color = entity.getColor();
        DanmakuType type = entity.getDanmakuType();
        // 材质宽度
        int width = 416;
        // 材质长度
        int length = 128;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double startU = 32 * color.ordinal();
        double startV = 32 * type.ordinal();

        poseStack.pushPose();
        poseStack.translate(0, 0.1, 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

        VertexConsumer buffer = bufferIn.getBuffer(RENDER_TYPE);
        PoseStack.Pose poseStackLast = poseStack.last();
        Matrix4f pose = poseStackLast.pose();
        Matrix3f normal = poseStackLast.normal();

        vertex(buffer, pose, normal, -type.getSize(), type.getSize(), (startU + 0) / width, (startV + 0) / length, packedLightIn);
        vertex(buffer, pose, normal, -type.getSize(), -type.getSize(), (startU + 0) / width, (startV + 32) / length, packedLightIn);
        vertex(buffer, pose, normal, type.getSize(), -type.getSize(), (startU + 32) / width, (startV + 32) / length, packedLightIn);
        vertex(buffer, pose, normal, type.getSize(), type.getSize(), (startU + 32) / width, (startV + 0) / length, packedLightIn);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDanmaku entity) {
        return DANMAKU_TEXTURE;
    }
}
