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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MaidFishingHookRenderer<T extends MaidFishingHook> extends EntityRenderer<T> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/fishing_hook.png");
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
        PoseStack.Pose lastedPose = poseStack.last();

        VertexConsumer consumer = buffer.getBuffer(RENDER_TYPE);
        vertex(consumer, lastedPose, packedLight, 0.0F, 0, 0, 1);
        vertex(consumer, lastedPose, packedLight, 1.0F, 0, 1, 1);
        vertex(consumer, lastedPose, packedLight, 1.0F, 1, 1, 0);
        vertex(consumer, lastedPose, packedLight, 0.0F, 1, 0, 0);

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

    protected void vertex(VertexConsumer consumer, PoseStack.Pose pose, int lightMapUV, float pX, int pY, int pU, int pV) {
        consumer.addVertex(pose, pX - 0.5F, pY - 0.5F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv((float) pU, (float) pV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightMapUV)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    protected static void renderPosTexture(VertexConsumer builder, PoseStack.Pose pose, int lightMapUV, float x, int y, int u, int v) {
        builder.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv((float) u, (float) v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightMapUV)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    protected static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightMapUV, float x, int y, int u, int v, float r, float g, float b) {
        builder.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F)
                .setColor(r, g, b, 1.0F)
                .setUv((float) u, (float) v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightMapUV)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
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

        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, 1.0F)
                .setNormal(pose, nx, ny, nz);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TEXTURE_LOCATION;
    }
}
