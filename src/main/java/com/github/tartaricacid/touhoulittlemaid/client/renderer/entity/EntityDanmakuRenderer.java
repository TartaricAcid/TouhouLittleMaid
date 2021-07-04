package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class EntityDanmakuRenderer extends EntityRenderer<EntityDanmaku> {
    private static final ResourceLocation DANMAKU_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/danmaku.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(DANMAKU_TEXTURE);

    public EntityDanmakuRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    private static void vertex(IVertexBuilder bufferIn, Matrix4f pose, Matrix3f normal, double x, double y, double texU, double texV, int packedLight) {
        bufferIn.vertex(pose, (float) x, (float) y, 0.0F).color(255, 255, 255, 255).uv((float) texU, (float) texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public void render(EntityDanmaku entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
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

        matrixStack.pushPose();
        matrixStack.translate(0, 0.1, 0);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

        IVertexBuilder buffer = bufferIn.getBuffer(RENDER_TYPE);
        MatrixStack.Entry matrixStackLast = matrixStack.last();
        Matrix4f pose = matrixStackLast.pose();
        Matrix3f normal = matrixStackLast.normal();

        vertex(buffer, pose, normal, -type.getSize(), type.getSize(), (startU + 0) / width, (startV + 0) / length, packedLightIn);
        vertex(buffer, pose, normal, -type.getSize(), -type.getSize(), (startU + 0) / width, (startV + 32) / length, packedLightIn);
        vertex(buffer, pose, normal, type.getSize(), -type.getSize(), (startU + 32) / width, (startV + 32) / length, packedLightIn);
        vertex(buffer, pose, normal, type.getSize(), type.getSize(), (startU + 32) / width, (startV + 0) / length, packedLightIn);
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDanmaku entity) {
        return DANMAKU_TEXTURE;
    }
}
