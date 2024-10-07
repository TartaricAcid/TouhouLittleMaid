package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class MaidFishingHookRenderer extends EntityRenderer<MaidFishingHook> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);
    private static final double VIEW_BOBBING_SCALE = 960.0D;

    public MaidFishingHookRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(MaidFishingHook pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        EntityMaid maid = pEntity.getPlayerOwner();
        if (maid != null) {
            pPoseStack.pushPose();
            pPoseStack.pushPose();
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
            pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            PoseStack.Pose posestack$pose = pPoseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RENDER_TYPE);
            vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 0, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 0, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 1, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 1, 0, 0);
            pPoseStack.popPose();
            int i = maid.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack itemstack = maid.getMainHandItem();
            if (!itemstack.canPerformAction(net.minecraftforge.common.ToolActions.FISHING_ROD_CAST)) {
                i = -i;
            }

            float f = maid.getAttackAnim(pPartialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(pPartialTicks, maid.yBodyRotO, maid.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = (double) Mth.sin(f2);
            double d1 = (double) Mth.cos(f2);
            double d2 = (double) i * 0.35D;
            double d3 = 0.8D;
            double d4;
            double d5;
            double d6;
            float f3;

            {
                d4 = Mth.lerp((double) pPartialTicks, maid.xo, maid.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = maid.yo + (double) maid.getEyeHeight() + (maid.getY() - maid.yo) * (double) pPartialTicks - 0.45D;
                d6 = Mth.lerp((double) pPartialTicks, maid.zo, maid.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = -0.1875F;
            }

            double d9 = Mth.lerp((double) pPartialTicks, pEntity.xo, pEntity.getX());
            double d10 = Mth.lerp((double) pPartialTicks, pEntity.yo, pEntity.getY()) + 0.25D;
            double d8 = Mth.lerp((double) pPartialTicks, pEntity.zo, pEntity.getZ());
            float f4 = (float) (d4 - d9);
            float f5 = (float) (d5 - d10) + f3;
            float f6 = (float) (d6 - d8);
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = pPoseStack.last();
            int j = 16;

            for (int k = 0; k <= 16; ++k) {
                stringVertex(f4, f5, f6, vertexconsumer1, posestack$pose1, fraction(k, 16), fraction(k + 1, 16));
            }

            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    private static float fraction(int pNumerator, int pDenominator) {
        return (float) pNumerator / (float) pDenominator;
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal, int pLightmapUV, float pX, int pY, int pU, int pV) {
        pConsumer.vertex(pPose, pX - 0.5F, (float) pY - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) pU, (float) pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pLightmapUV).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void stringVertex(float pX, float pY, float pZ, VertexConsumer pConsumer, PoseStack.Pose pPose, float p_174124_, float p_174125_) {
        float f = pX * p_174124_;
        float f1 = pY * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = pZ * p_174124_;
        float f3 = pX * p_174125_ - f;
        float f4 = pY * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = pZ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        pConsumer.vertex(pPose.pose(), f, f1, f2).color(0, 0, 0, 255).normal(pPose.normal(), f3, f4, f5).endVertex();
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(MaidFishingHook pEntity) {
        return TEXTURE_LOCATION;
    }
}
