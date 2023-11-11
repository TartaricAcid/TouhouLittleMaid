package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class ReplaceExperienceOrbRenderer extends EntityRenderer<ExperienceOrbEntity> {
    private static final ResourceLocation POINT_ITEM_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/point_item.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(POINT_ITEM_TEXTURE);
    private final ExperienceOrbRenderer vanillaRender;

    public ReplaceExperienceOrbRenderer(EntityRendererManager context) {
        super(context);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
        this.vanillaRender = new ExperienceOrbRenderer(context);
    }

    private static void vertex(IVertexBuilder pConsumer, Matrix4f pMatrix, Matrix3f pMatrixNormal, float pX, float pY, int pRed, int pGreen, int pBlue, float pTexU, float pTexV, int pPackedLight) {
        pConsumer.vertex(pMatrix, pX, pY, 0.0F).color(pRed, pGreen, pBlue, 128).uv(pTexU, pTexV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(pMatrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    protected int getBlockLightLevel(ExperienceOrbEntity pEntity, BlockPos pPos) {
        return MathHelper.clamp(super.getBlockLightLevel(pEntity, pPos) + 7, 0, 15);
    }

    @Override
    public void render(ExperienceOrbEntity orb, float pEntityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight) {
        if (VanillaConfig.REPLACE_XP_TEXTURE.get()) {
            renderPointItem(orb, pEntityYaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            vanillaRender.render(orb, pEntityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    private void renderPointItem(ExperienceOrbEntity orb, float pEntityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight) {
        poseStack.pushPose();
        int icon = orb.getIcon();
        float texU1 = (float) (icon % 4 * 16) / 64.0F;
        float texU2 = (float) (icon % 4 * 16 + 16) / 64.0F;
        float texV2 = (float) (icon / 4 * 16) / 64.0F;
        float texV1 = (float) (icon / 4 * 16 + 16) / 64.0F;
        poseStack.translate(0.0F, 0.1F, 0.0F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        poseStack.scale(0.3F, 0.3F, 0.3F);
        IVertexBuilder consumer = buffer.getBuffer(RENDER_TYPE);
        MatrixStack.Entry lasted = poseStack.last();
        Matrix4f pose = lasted.pose();
        Matrix3f normal = lasted.normal();
        vertex(consumer, pose, normal, -0.5F, -0.25F, 255, 255, 255, texU1, texV1, packedLight);
        vertex(consumer, pose, normal, 0.5F, -0.25F, 255, 255, 255, texU2, texV1, packedLight);
        vertex(consumer, pose, normal, 0.5F, 0.75F, 255, 255, 255, texU2, texV2, packedLight);
        vertex(consumer, pose, normal, -0.5F, 0.75F, 255, 255, 255, texU1, texV2, packedLight);
        poseStack.popPose();
        super.render(orb, pEntityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(ExperienceOrbEntity pEntity) {
        return POINT_ITEM_TEXTURE;
    }
}
