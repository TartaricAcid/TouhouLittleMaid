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
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class MaidFishingHookRenderer<T extends MaidFishingHook> extends EntityRenderer<T> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);

    public MaidFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T fishingHook, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        EntityMaid maid = fishingHook.getMaidOwner();
        if (maid == null) {
            return;
        }
        poseStack.pushPose();
        this.renderBobber(fishingHook, poseStack, buffer, packedLight);
        this.renderFishingLine(fishingHook, partialTicks, poseStack, buffer, maid);
        poseStack.popPose();
        super.render(fishingHook, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderBobber(T fishingHook, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        PoseStack.Pose lasted = poseStack.last();
        Matrix4f lastedPose = lasted.pose();
        Matrix3f lastedNormal = lasted.normal();

        VertexConsumer consumer = buffer.getBuffer(RENDER_TYPE);
        vertex(consumer, lastedPose, lastedNormal, packedLight, 0.0F, 0, 0, 1);
        vertex(consumer, lastedPose, lastedNormal, packedLight, 1.0F, 0, 1, 1);
        vertex(consumer, lastedPose, lastedNormal, packedLight, 1.0F, 1, 1, 0);
        vertex(consumer, lastedPose, lastedNormal, packedLight, 0.0F, 1, 0, 0);

        poseStack.popPose();
    }

    protected float[] getLineColor(T fishingHook) {
        return new float[]{0f, 0f, 0f};
    }

    protected void renderFishingLine(T fishingHook, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, EntityMaid maid) {
        float lerpBodyRot;
        if (maid.getVehicle() instanceof LivingEntity vehicle) {
            lerpBodyRot = Mth.lerp(partialTicks, vehicle.yBodyRotO, vehicle.yBodyRot) * ((float) Math.PI / 180F);
        } else {
            lerpBodyRot = Mth.lerp(partialTicks, maid.yBodyRotO, maid.yBodyRot) * ((float) Math.PI / 180F);
        }
        double sin = Mth.sin(lerpBodyRot);
        double cos = Mth.cos(lerpBodyRot);

        double x1 = Mth.lerp(partialTicks, maid.xo, maid.getX()) - cos * 0.35D - sin * 0.8D;
        double y1 = maid.yo + maid.getEyeHeight() + (maid.getY() - maid.yo) * partialTicks - 0.45D;
        double z1 = Mth.lerp(partialTicks, maid.zo, maid.getZ()) - sin * 0.35D + cos * 0.8D;

        double x2 = Mth.lerp(partialTicks, fishingHook.xo, fishingHook.getX());
        double y2 = Mth.lerp(partialTicks, fishingHook.yo, fishingHook.getY()) + 0.25D;
        double z2 = Mth.lerp(partialTicks, fishingHook.zo, fishingHook.getZ());

        float x = (float) (x1 - x2);
        float y = (float) (y1 - y2) - 0.1875F;
        float z = (float) (z1 - z2);

        float[] colors = getLineColor(fishingHook);

        VertexConsumer lineConsumer = buffer.getBuffer(RenderType.lineStrip());
        PoseStack.Pose lasted = poseStack.last();
        for (int i = 0; i <= 16; ++i) {
            stringVertex(x, y, z, lineConsumer, lasted, fraction(i), fraction(i + 1), colors[0], colors[1], colors[2]);
        }
    }

    protected float fraction(int numerator) {
        return (float) numerator / (float) 16;
    }

    protected void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, int lightMapUV, float pX, int pY, int pU, int pV) {
        consumer.vertex(pose, pX - 0.5F, pY - 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv((float) pU, (float) pV)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightMapUV)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    protected static void renderPosTexture(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, int lightMapUV, float x, int y, int u, int v) {
        builder.vertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv((float) u, (float) v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightMapUV)
                .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    protected static void vertex(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, int lightMapUV, float x, int y, int u, int v, float r, float g, float b) {
        builder.vertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F)
                .color(r, g, b, 1.0F)
                .uv((float) u, (float) v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightMapUV).normal(matrix3f, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    protected void stringVertex(float pX, float pY, float pZ, VertexConsumer consumer, PoseStack.Pose pose, float fraction1, float fraction2, float r, float g, float b) {
        float x = pX * fraction1;
        float y = pY * (fraction1 * fraction1 + fraction1) * 0.5F + 0.25F;
        float z = pZ * fraction1;

        float nx = pX * fraction2 - x;
        float ny = pY * (fraction2 * fraction2 + fraction2) * 0.5F + 0.25F - y;
        float nz = pZ * fraction2 - z;
        float sqrt = Mth.sqrt(nx * nx + ny * ny + nz * nz);

        nx /= sqrt;
        ny /= sqrt;
        nz /= sqrt;

        consumer.vertex(pose.pose(), x, y, z)
                .color(r, g, b, 1.0F)
                .normal(pose.normal(), nx, ny, nz)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TEXTURE_LOCATION;
    }
}
