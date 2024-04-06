package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Color;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.data.EntityModelData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.EModelRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.IRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class GeoReplacedEntityRenderer<T extends IAnimatable> extends EntityRenderer implements IGeoRenderer {
    protected static final Map<Class<? extends IAnimatable>, GeoReplacedEntityRenderer> renderers = new ConcurrentHashMap<>();

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            GeoReplacedEntityRenderer renderer = renderers.get(object.getClass());
            return renderer == null ? null : renderer.getGeoModelProvider();
        });
    }

    protected final AnimatedGeoModel<IAnimatable> modelProvider;
    protected final List<GeoLayerRenderer> layerRenderers = new ObjectArrayList<>();
    protected T animatable;
    protected IAnimatable currentAnimatable;
    protected float widthScale = 1;
    protected float heightScale = 1;
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    protected MultiBufferSource rtb = null;
    private IRenderCycle currentModelRenderCycle = EModelRenderCycle.INITIAL;

    public GeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager,
                                     AnimatedGeoModel<IAnimatable> modelProvider, T animatable) {
        super(renderManager);
        this.modelProvider = modelProvider;
        this.animatable = animatable;
        renderers.putIfAbsent(animatable.getClass(), this);
    }

    public static GeoReplacedEntityRenderer getRenderer(Class<? extends IAnimatable> animatableClass) {
        return renderers.get(animatableClass);
    }

    public static int getPackedOverlay(LivingEntity entity, float u) {
        return OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    private static float getFacingAngle(Direction facingIn) {
        return switch (facingIn) {
            case SOUTH -> 90;
            case NORTH -> 270;
            case EAST -> 180;
            default -> 0;
        };
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif,
                                         float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight,
                                         int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = segment / 24f;
        int lerpBlockLight = (int) Mth.lerp(piecePosPercent, entityBlockLight, holderBlockLight);
        int lerpSkyLight = (int) Mth.lerp(piecePosPercent, entitySkyLight, holderSkyLight);
        int packedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1f;
        float red = 0.5f * knotColourMod;
        float green = 0.4f * knotColourMod;
        float blue = 0.3f * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0f ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0f - piecePosPercent) * (1.0f - piecePosPercent);
        float z = zDif * piecePosPercent;

        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1).uv2(packedLight).endVertex();
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1).uv2(packedLight).endVertex();
    }

    @Override
    @Nonnull
    public IRenderCycle getCurrentModelRenderCycle() {
        return this.currentModelRenderCycle;
    }

    @Override
    public void setCurrentModelRenderCycle(IRenderCycle currentModelRenderCycle) {
        this.currentModelRenderCycle = currentModelRenderCycle;
    }

    @Override
    public float getWidthScale(Object animatable) {
        return this.widthScale;
    }

    @Override
    public float getHeightScale(Object entity) {
        return this.heightScale;
    }

    @Override
    public void renderEarly(Object animatable, PoseStack poseStack, float partialTick,
                            MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlayIn,
                            float red, float green, float blue, float alpha) {
        this.renderEarlyMat = new Matrix4f(poseStack.last().pose());
        IGeoRenderer.super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        render(entity, this.animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        if (!(entity instanceof LivingEntity livingEntity)) {
            throw new IllegalStateException("Replaced renderer was not an instanceof LivingEntity");
        }

        this.currentAnimatable = animatable;
        this.dispatchedMat = new Matrix4f(poseStack.last().pose());
        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());

        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        poseStack.pushPose();
        if (entity instanceof Mob mob) {
            Entity leashHolder = mob.getLeashHolder();
            if (leashHolder != null) {
                renderLeash(mob, partialTick, poseStack, bufferSource, leashHolder);
            }
        }

        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = livingEntity.isBaby();

        float lerpBodyRot = Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float lerpHeadRot = Mth.rotLerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;

        if (shouldSit && entity.getVehicle() instanceof LivingEntity vehicle) {
            lerpBodyRot = Mth.rotLerp(partialTick, vehicle.yBodyRotO, vehicle.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;
            if (clampedHeadYaw * clampedHeadYaw > 2500f) {
                lerpBodyRot += clampedHeadYaw * 0.2f;
            }
            netHeadYaw = lerpHeadRot - lerpBodyRot;
        }

        if (entity.getPose() == Pose.SLEEPING) {
            Direction direction = livingEntity.getBedOrientation();
            if (direction != null) {
                float eyeOffset = entity.getEyeHeight(Pose.STANDING) - 0.1f;
                poseStack.translate(-direction.getStepX() * eyeOffset, 0, -direction.getStepZ() * eyeOffset);
            }
        }

        float lerpedAge = livingEntity.tickCount + partialTick;
        float limbSwingAmount = 0;
        float limbSwing = 0;

        applyRotations(livingEntity, poseStack, lerpedAge, lerpBodyRot, partialTick);
        preRenderCallback(livingEntity, poseStack, partialTick);
        if (!shouldSit && entity.isAlive()) {
            limbSwingAmount = livingEntity.walkAnimation.speed(partialTick);
            limbSwing = livingEntity.walkAnimation.position(partialTick);
            if (livingEntity.isBaby()) {
                limbSwing *= 3.0F;
            }
        }

        float headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(animatable));
        AnimationEvent predicate = new AnimationEvent(animatable, limbSwing, limbSwingAmount, partialTick,
                (limbSwingAmount <= -getSwingMotionAnimThreshold() || limbSwingAmount <= getSwingMotionAnimThreshold()), Collections.singletonList(entityModelData));

        this.modelProvider.setCustomAnimations(animatable, getInstanceId(entity), predicate);
        poseStack.translate(0, 0.01f, 0);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));

        Color renderColor = getRenderColor(animatable, partialTick, poseStack, bufferSource, null, packedLight);
        RenderType renderType = getRenderType(entity, partialTick, poseStack, bufferSource, null, packedLight,
                getTextureLocation(entity));

        if (Minecraft.getInstance().player != null && !entity.isInvisibleTo(Minecraft.getInstance().player)) {
            VertexConsumer translucentBuffer = bufferSource.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity)));
            render(model, entity, partialTick, renderType, poseStack, bufferSource, translucentBuffer,
                    packedLight, getPackedOverlay(livingEntity, getOverlayProgress(livingEntity, partialTick)),
                    renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
        }
        if (!entity.isSpectator()) {
            for (GeoLayerRenderer layerRenderer : this.layerRenderers) {
                layerRenderer.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTick,
                        lerpedAge, netHeadYaw, headPitch);
            }
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingXform()) {
            Entity entity = (Entity) this.animatable;
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);
            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(getRenderOffset(entity, 1).toVector3f());
            bone.setLocalSpaceXform(localMatrix);
            Matrix4f worldState = new Matrix4f(localMatrix);
            worldState.translate(entity.position().toVector3f());
            bone.setWorldSpaceXform(worldState);
        }
        IGeoRenderer.super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                alpha);
    }

    protected float getOverlayProgress(LivingEntity entity, float partialTicks) {
        return 0.0F;
    }

    protected void preRenderCallback(LivingEntity entity, PoseStack poseStack, float partialTick) {
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return this.modelProvider.getTextureLocation(this.currentAnimatable);
    }

    @Override
    public AnimatedGeoModel getGeoModelProvider() {
        return this.modelProvider;
    }

    protected void applyRotations(LivingEntity entity, PoseStack poseStack, float ageInTicks,
                                  float rotationYaw, float partialTick) {
        Pose pose = entity.getPose();
        if (pose != Pose.SLEEPING) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180f - rotationYaw));
        }
        if (pose == Pose.SLEEPING) {
            Direction bedOrientation = entity.getBedOrientation();
            poseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? getFacingAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(entity)));
            poseStack.mulPose(Axis.YP.rotationDegrees(270f));
            poseStack.translate(0, -0.25, 0);
        } else if (entity.hasCustomName() || entity instanceof Player) {
            String name = entity.getName().getString();
            if (entity instanceof Player player) {
                if (!player.isModelPartShown(PlayerModelPart.CAPE)) {
                    return;
                }
            } else {
                name = ChatFormatting.stripFormatting(name);
            }
            if (name != null && ("Dinnerbone".equals(name) || "Grumm".equalsIgnoreCase(name))) {
                poseStack.translate(0, entity.getBbHeight() + 0.1f, 0);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            }
        }
    }

    protected boolean isVisible(LivingEntity entity) {
        return !entity.isInvisible();
    }

    protected float getDeathMaxRotation(LivingEntity entity) {
        return 90;
    }

    @Override
    public boolean shouldShowName(Entity entity) {
        double nameRenderDistance = entity.isDiscrete() ? 32d : 64d;
        if (this.entityRenderDispatcher.distanceToSqr(entity) >= nameRenderDistance * nameRenderDistance) {
            return false;
        }
        return entity.shouldShowName() || (entity == this.entityRenderDispatcher.crosshairPickEntity && entity.hasCustomName())
                && Minecraft.renderNames();
    }

    protected float getSwingProgress(LivingEntity entity, float partialTick) {
        return entity.getAttackAnim(partialTick);
    }

    protected float getSwingMotionAnimThreshold() {
        return 0.15f;
    }

    @Override
    public ResourceLocation getTextureLocation(Object animatable) {
        return this.modelProvider.getTextureLocation((IAnimatable) animatable);
    }

    public final boolean addLayer(GeoLayerRenderer<? extends LivingEntity> layer) {
        return this.layerRenderers.add(layer);
    }

    public <E extends Entity> void renderLeash(Mob entity, float partialTick, PoseStack poseStack,
                                               MultiBufferSource bufferSource, E leashHolder) {
        double lerpBodyAngle = (Mth.lerp(partialTick, entity.yBodyRot, entity.yBodyRotO) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
        Vec3 leashOffset = entity.getLeashOffset(1.0f);
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = Mth.lerp(partialTick, entity.xo, entity.getX()) + xAngleOffset;
        double lerpOriginY = Mth.lerp(partialTick, entity.yo, entity.getY()) + leashOffset.y;
        double lerpOriginZ = Mth.lerp(partialTick, entity.zo, entity.getZ()) + zAngleOffset;
        Vec3 ropeGripPosition = leashHolder.getRopeHoldPosition(partialTick);
        float xDif = (float) (ropeGripPosition.x - lerpOriginX);
        float yDif = (float) (ropeGripPosition.y - lerpOriginY);
        float zDif = (float) (ropeGripPosition.z - lerpOriginZ);
        float offsetMod = (float) Mth.fastInvSqrt(xDif * xDif + zDif * zDif) * 0.025f / 2f;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        BlockPos entityEyePos = BlockPos.containing(entity.getEyePosition(partialTick));
        BlockPos holderEyePos = BlockPos.containing(leashHolder.getEyePosition(partialTick));
        int entityBlockLight = getBlockLightLevel(entity, entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.level().getBrightness(LightLayer.BLOCK, holderEyePos);
        int entitySkyLight = entity.level().getBrightness(LightLayer.SKY, entityEyePos);
        int holderSkyLight = entity.level().getBrightness(LightLayer.SKY, holderEyePos);

        poseStack.pushPose();
        poseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);
        Matrix4f posMatrix = poseStack.last().pose();
        for (int segment = 0; segment <= 24; ++segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.025f, xOffset, zOffset, segment, false);
        }
        for (int segment = 24; segment >= 0; --segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.0f, xOffset, zOffset, segment, true);
        }
        poseStack.popPose();
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public void setCurrentRTB(MultiBufferSource bufferSource) {
        this.rtb = bufferSource;
    }
}
