package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
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

public class EntityPowerPointRenderer extends EntityRenderer<EntityPowerPoint> {
    private static final ResourceLocation POWER_POINT_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(POWER_POINT_TEXTURES);

    public EntityPowerPointRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    private static void vertex(IVertexBuilder bufferIn, Matrix4f pose, Matrix3f normal, double x, double y, double texU, double texV, int packedLight) {
        bufferIn.vertex(pose, (float) x, (float) y, 0.0F).color(255, 255, 255, 255).uv((float) texU, (float) texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public void render(EntityPowerPoint entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        int texIndex = entity.getIcon();
        int remainder = texIndex % 4;
        int quotient = texIndex / 4;
        double texPos1 = remainder * 16 / 64.0;
        double texPos2 = (remainder * 16 + 16) / 64.0;
        double texPos3 = quotient * 16 / 64.0;
        double texPos4 = (quotient * 16 + 16) / 64.0;

        matrixStack.pushPose();
        matrixStack.translate(0, 0.1, 0);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrixStack.scale(0.3F, 0.3F, 0.3F);

        IVertexBuilder buffer = bufferIn.getBuffer(RENDER_TYPE);
        MatrixStack.Entry matrixStackLast = matrixStack.last();
        Matrix4f pose = matrixStackLast.pose();
        Matrix3f normal = matrixStackLast.normal();

        vertex(buffer, pose, normal, -1, -0.25, texPos1, texPos4, packedLightIn);
        vertex(buffer, pose, normal, 1, -0.25, texPos2, texPos4, packedLightIn);
        vertex(buffer, pose, normal, 1, 1.75, texPos2, texPos3, packedLightIn);
        vertex(buffer, pose, normal, -1, 1.75, texPos1, texPos3, packedLightIn);
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPowerPoint entity) {
        return POWER_POINT_TEXTURES;
    }
}
