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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;

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
    protected IRenderTypeBuffer rtb = null;
    private IRenderCycle currentModelRenderCycle = EModelRenderCycle.INITIAL;

    public GeoReplacedEntityRenderer(EntityRendererManager renderManager,
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
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    private static void renderLeashPiece(IVertexBuilder buffer, Matrix4f positionMatrix, float xDif, float yDif,
                                         float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight,
                                         int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = segment / 24f;
        int lerpBlockLight = (int) MathHelper.lerp(piecePosPercent, entityBlockLight, holderBlockLight);
        int lerpSkyLight = (int) MathHelper.lerp(piecePosPercent, entitySkyLight, holderSkyLight);
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
        public void renderEarly(Object animatable, MatrixStack poseStack, float partialTick,
                            IRenderTypeBuffer bufferSource, IVertexBuilder buffer, int packedLight, int packedOverlayIn,
                            float red, float green, float blue, float alpha) {
        this.renderEarlyMat = poseStack.last().pose().copy();
        IGeoRenderer.super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
        public void render(Entity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       IRenderTypeBuffer bufferSource, int packedLight) {

        render(entity, this.animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, MatrixStack poseStack,
                       IRenderTypeBuffer bufferSource, int packedLight) {

        if (!(entity instanceof LivingEntity)) {
            throw new IllegalStateException("Replaced renderer was not an instanceof LivingEntity");
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        this.currentAnimatable = animatable;
        this.dispatchedMat = poseStack.last().pose().copy();
        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());

        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        poseStack.pushPose();
        if (entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            Entity leashHolder = mob.getLeashHolder();
            if (leashHolder != null) {
                renderLeash(mob, partialTick, poseStack, bufferSource, leashHolder);
            }
        }

        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = livingEntity.isBaby();

        float lerpBodyRot = MathHelper.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float lerpHeadRot = MathHelper.rotLerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;

        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity vehicle = (LivingEntity) entity.getVehicle();
            lerpBodyRot = MathHelper.rotLerp(partialTick, vehicle.yBodyRotO, vehicle.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = MathHelper.clamp(MathHelper.wrapDegrees(netHeadYaw), -85, 85);
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
            limbSwingAmount = Math.min(1, MathHelper.lerp(partialTick, livingEntity.animationSpeedOld, livingEntity.animationSpeed));
            limbSwing = livingEntity.animationPosition - livingEntity.animationSpeed * (1 - partialTick);
            if (livingEntity.isBaby()) {
                limbSwing *= 3.0F;
            }
        }

        float headPitch = MathHelper.lerp(partialTick, entity.xRotO, entity.xRot);
        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -MathHelper.clamp(MathHelper.wrapDegrees(netHeadYaw), -85, 85);
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(animatable));
        AnimationEvent predicate = new AnimationEvent(animatable, limbSwing, limbSwingAmount, partialTick,
                (limbSwingAmount <= -getSwingMotionAniMathHelperreshold() || limbSwingAmount <= getSwingMotionAniMathHelperreshold()), Collections.singletonList(entityModelData));

        this.modelProvider.setCustomAnimations(animatable, getInstanceId(entity), predicate);
        poseStack.translate(0, 0.01f, 0);
        Minecraft.getInstance().textureManager.bind(getTextureLocation(entity));

        Color renderColor = getRenderColor(animatable, partialTick, poseStack, bufferSource, null, packedLight);
        RenderType renderType = getRenderType(entity, partialTick, poseStack, bufferSource, null, packedLight,
                getTextureLocation(entity));

        if (Minecraft.getInstance().player != null && !entity.isInvisibleTo(Minecraft.getInstance().player)) {
            IVertexBuilder glintBuffer = bufferSource.getBuffer(RenderType.entityGlintDirect());
            IVertexBuilder translucentBuffer = bufferSource
                    .getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity)));
            render(model, entity, partialTick, renderType, poseStack, bufferSource,
                    glintBuffer != translucentBuffer ? VertexBuilderUtils.create(glintBuffer, translucentBuffer)
                            : null,
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
        public void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingXform()) {
            Entity entity = (Entity) this.animatable;
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);
            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(entity, 1)));
            bone.setLocalSpaceXform(localMatrix);
            Matrix4f worldState = localMatrix.copy();
            worldState.translate(new Vector3f(entity.position()));
            bone.setWorldSpaceXform(worldState);
        }
        IGeoRenderer.super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                alpha);
    }

    protected float getOverlayProgress(LivingEntity entity, float partialTicks) {
        return 0.0F;
    }

    protected void preRenderCallback(LivingEntity entity, MatrixStack poseStack, float partialTick) {
    }

    @Override
        public ResourceLocation getTextureLocation(Entity entity) {
        return this.modelProvider.getTextureLocation(this.currentAnimatable);
    }

    @Override
        public AnimatedGeoModel getGeoModelProvider() {
        return this.modelProvider;
    }

    protected void applyRotations(LivingEntity entity, MatrixStack poseStack, float ageInTicks,
                                  float rotationYaw, float partialTick) {
        Pose pose = entity.getPose();
        if (pose != Pose.SLEEPING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180f - rotationYaw));
        }
        if (pose == Pose.SLEEPING) {
            Direction bedOrientation = entity.getBedOrientation();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(bedOrientation != null ? getFacingAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(getDeathMaxRotation(entity)));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(270f));
            poseStack.translate(0, -0.25, 0);
        } else if (entity.hasCustomName() || entity instanceof PlayerEntity) {
            String name = entity.getName().getString();
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (!player.isModelPartShown(PlayerModelPart.CAPE)) {
                    return;
                }
            } else {
                name = TextFormatting.stripFormatting(name);
            }
            if (name != null && ("Dinnerbone".equals(name) || "Grumm".equalsIgnoreCase(name))) {
                poseStack.translate(0, entity.getBbHeight() + 0.1f, 0);
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180f));
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
        return entity == this.entityRenderDispatcher.crosshairPickEntity && entity.hasCustomName() && Minecraft.renderNames();
    }

    protected float getSwingProgress(LivingEntity entity, float partialTick) {
        return entity.getAttackAnim(partialTick);
    }

    protected float getSwingMotionAniMathHelperreshold() {
        return 0.15f;
    }

    @Override
        public ResourceLocation getTextureLocation(Object animatable) {
        return this.modelProvider.getTextureLocation((IAnimatable) animatable);
    }

    public final boolean addLayer(GeoLayerRenderer<? extends LivingEntity> layer) {
        return this.layerRenderers.add(layer);
    }

    public <E extends Entity> void renderLeash(MobEntity entity, float partialTicks, MatrixStack poseStack,
                                               IRenderTypeBuffer buffer, E leashHolder) {
        int u;
        poseStack.pushPose();
        Vector3d vec3d = leashHolder.getRopeHoldPosition(partialTicks);
        double d = (double) (MathHelper.lerp(partialTicks, entity.yBodyRot, entity.yBodyRotO) * ((float) Math.PI / 180))
                + 1.5707963267948966;
        Vector3d vec3d2 = ((Entity) entity).getLeashOffset();
        double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
        double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
        double g = MathHelper.lerp(partialTicks, entity.xo, entity.getX()) + e;
        double h = MathHelper.lerp(partialTicks, entity.yo, entity.getY()) + vec3d2.y;
        double i = MathHelper.lerp(partialTicks, entity.zo, entity.getZ()) + f;
        poseStack.translate(e, vec3d2.y, f);
        float j = (float) (vec3d.x - g);
        float k = (float) (vec3d.y - h);
        float l = (float) (vec3d.z - i);
        IVertexBuilder vertexConsumer = buffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();
        float n = MathHelper.fastInvSqrt(j * j + l * l) * 0.025f / 2.0f;
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = new BlockPos(entity.getEyePosition(partialTicks));
        BlockPos blockPos2 = new BlockPos(leashHolder.getEyePosition(partialTicks));
        int q = this.getBlockLightLevel(entity, blockPos);
        int r = leashHolder.isOnFire() ? 15 : leashHolder.level.getBrightness(LightType.BLOCK, blockPos2);
        int s = entity.level.getBrightness(LightType.SKY, blockPos);
        int t = entity.level.getBrightness(LightType.SKY, blockPos2);
        for (u = 0; u <= 24; ++u) {
            GeoReplacedEntityRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o,
                    p, u, false);
        }
        for (u = 24; u >= 0; --u) {
            GeoReplacedEntityRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.0f, o,
                    p, u, true);
        }
        poseStack.popPose();
    }

    @Override
        public IRenderTypeBuffer getCurrentRTB() {
        return this.rtb;
    }

    @Override
        public void setCurrentRTB(IRenderTypeBuffer bufferSource) {
        this.rtb = bufferSource;
    }
}
