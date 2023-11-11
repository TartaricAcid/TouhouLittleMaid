package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatableModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Color;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoCube;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.GeoModelProvider;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.data.EntityModelData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.AnimationUtils;
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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class GeoEntityRenderer<T extends LivingEntity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof Entity ? (IAnimatableModel<Object>) AnimationUtils.getGeoModelForEntity((Entity) animatable) : null);
    }

    protected final AnimatedGeoModel<T> modelProvider;
    protected final List<GeoLayerRenderer<T>> layerRenderers = new ObjectArrayList<>();
    public ItemStack mainHand, offHand, helmet, chestplate, leggings, boots;
    public IRenderTypeBuffer rtb;
    public ResourceLocation whTexture;
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    protected T animatable;
    protected float widthScale = 1;
    protected float heightScale = 1;
    private IRenderCycle currentModelRenderCycle = EModelRenderCycle.INITIAL;

    public GeoEntityRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager);
        this.modelProvider = modelProvider;
    }

    private static float getFacingAngle(Direction direction) {
        switch (direction) {
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
    public void renderEarly(T animatable, MatrixStack poseStack, float partialTick, IRenderTypeBuffer bufferSource,
                            IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                            float partialTicks) {
        this.animatable = animatable;
        this.renderEarlyMat = poseStack.last().pose().copy();
        this.rtb = bufferSource;
        this.whTexture = getTextureLocation(animatable);
        this.mainHand = animatable.getItemBySlot(EquipmentSlotType.MAINHAND);
        this.offHand = animatable.getItemBySlot(EquipmentSlotType.OFFHAND);
        this.helmet = animatable.getItemBySlot(EquipmentSlotType.HEAD);
        this.chestplate = animatable.getItemBySlot(EquipmentSlotType.CHEST);
        this.leggings = animatable.getItemBySlot(EquipmentSlotType.LEGS);
        this.boots = animatable.getItemBySlot(EquipmentSlotType.FEET);
        IGeoRenderer.super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight,
                packedOverlay, red, green, blue, partialTicks);
    }

    @Override
    public void render(T animatable, float entityYaw, float partialTick, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight) {
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        poseStack.pushPose();
        if (animatable instanceof MobEntity) {
            MobEntity mob = (MobEntity) animatable;
            Entity leashHolder = mob.getLeashHolder();
            if (leashHolder != null) {
                renderLeash(animatable, partialTick, poseStack, bufferSource, leashHolder);
            }
        }

        this.dispatchedMat = poseStack.last().pose().copy();
        boolean shouldSit = animatable.isPassenger() && (animatable.getVehicle() != null && animatable.getVehicle().shouldRiderSit());
        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = animatable.isBaby();

        float lerpBodyRot = MathHelper.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
        float lerpHeadRot = MathHelper.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;

        if (shouldSit && animatable.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) animatable.getVehicle();
            lerpBodyRot = MathHelper.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = MathHelper.clamp(MathHelper.wrapDegrees(netHeadYaw), -85, 85);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;
            if (clampedHeadYaw * clampedHeadYaw > 2500f) {
                lerpBodyRot += clampedHeadYaw * 0.2f;
            }
            netHeadYaw = lerpHeadRot - lerpBodyRot;
        }

        if (animatable.getPose() == Pose.SLEEPING) {
            Direction bedDirection = animatable.getBedOrientation();
            if (bedDirection != null) {
                float eyePosOffset = animatable.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
            }
        }

        float ageInTicks = animatable.tickCount + partialTick;
        float limbSwingAmount = 0;
        float limbSwing = 0;
        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);
        if (!shouldSit && animatable.isAlive()) {
            limbSwingAmount = MathHelper.lerp(partialTick, animatable.animationSpeedOld, animatable.animationSpeed);
            limbSwing = animatable.animationPosition - animatable.animationSpeed * (1 - partialTick);
            if (animatable.isBaby()) {
                limbSwing *= 3f;
            }
            if (limbSwingAmount > 1f) {
                limbSwingAmount = 1f;
            }
        }

        float headPitch = MathHelper.lerp(partialTick, animatable.xRotO, animatable.xRot);
        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -netHeadYaw;

        AnimationEvent<T> predicate = new AnimationEvent<T>(animatable, limbSwing, limbSwingAmount, partialTick,
                (limbSwingAmount <= -getSwingMotionAniMathHelperreshold() || limbSwingAmount > getSwingMotionAniMathHelperreshold()), Collections.singletonList(entityModelData));
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(animatable));
        this.modelProvider.setCustomAnimations(animatable, getInstanceId(animatable), predicate);
        poseStack.translate(0, 0.01f, 0);
        Minecraft.getInstance().textureManager.bind(getTextureLocation(animatable));
        Color renderColor = getRenderColor(animatable, partialTick, poseStack, bufferSource, null, packedLight);
        RenderType renderType = getRenderType(animatable, partialTick, poseStack, bufferSource, null, packedLight,
                getTextureLocation(animatable));
        if (Minecraft.getInstance().player != null && !animatable.isInvisibleTo(Minecraft.getInstance().player)) {
            IVertexBuilder glintBuffer = bufferSource.getBuffer(RenderType.entityGlintDirect());
            IVertexBuilder translucentBuffer = bufferSource
                    .getBuffer(RenderType.entityTranslucentCull(getTextureLocation(animatable)));
            render(model, animatable, partialTick, renderType, poseStack, bufferSource,
                    glintBuffer != translucentBuffer ? VertexBuilderUtils.create(glintBuffer, translucentBuffer)
                            : null,
                    packedLight, getOverlay(animatable, 0), renderColor.getRed() / 255f,
                    renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
                    renderColor.getAlpha() / 255f);
        }

        if (!animatable.isSpectator()) {
            for (GeoLayerRenderer<T> layerRenderer : this.layerRenderers) {
                renderLayer(poseStack, bufferSource, packedLight, animatable, limbSwing, limbSwingAmount, partialTick, ageInTicks,
                        netHeadYaw, headPitch, bufferSource, layerRenderer);
            }
        }

        poseStack.popPose();
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        boolean rotOverride = bone.rotMat != null;
        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        } else {
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        }
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
            bone.setLocalSpaceXform(localMatrix);

            Matrix4f worldState = localMatrix.copy();

            worldState.translate(new Vector3f(this.animatable.position()));
            bone.setWorldSpaceXform(worldState);
        }
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        if (!bone.isHidden) {
            if (!bone.cubesAreHidden()) {
                for (GeoCube geoCube : bone.childCubes) {
                    poseStack.pushPose();
                    renderCube(geoCube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    poseStack.popPose();
                }
            }
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
        poseStack.popPose();
    }

    protected void renderLayer(MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight, T animatable,
                               float limbSwing, float limbSwingAmount, float partialTick, float rotFloat, float netHeadYaw,
                               float headPitch, IRenderTypeBuffer bufferSource2, GeoLayerRenderer<T> layerRenderer) {
        layerRenderer.render(poseStack, bufferSource, packedLight, animatable, limbSwing, limbSwingAmount, partialTick, rotFloat,
                netHeadYaw, headPitch);
    }

    @Override
    public int getInstanceId(T animatable) {
        return animatable.getId();
    }

    @Override
    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public float getWidthScale(T animatable) {
        return this.widthScale;
    }

    @Override
    public float getHeightScale(T entity) {
        return this.heightScale;
    }

    public int getOverlay(T entity, float u) {
        return OverlayTexture.pack(OverlayTexture.u(u),
                OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    protected void applyRotations(T animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw,
                                  float partialTick) {
        Pose pose = animatable.getPose();
        if (pose != Pose.SLEEPING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180f - rotationYaw));
        }
        if (animatable.deathTime > 0) {
            float deathRotation = (animatable.deathTime + partialTick - 1f) / 20f * 1.6f;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(Math.min(MathHelper.sqrt(deathRotation), 1) * getDeathMaxRotation(animatable)));
        } else if (animatable.isAutoSpinAttack()) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-90f - animatable.xRot));
            poseStack.mulPose(Vector3f.YP.rotationDegrees((animatable.tickCount + partialTick) * -75f));
        } else if (pose == Pose.SLEEPING) {
            Direction bedOrientation = animatable.getBedOrientation();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(bedOrientation != null ? getFacingAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(getDeathMaxRotation(animatable)));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(270f));
        } else if (animatable.hasCustomName() || animatable instanceof PlayerEntity) {
            String name = animatable.getName().getString();
            if (animatable instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) animatable;
                if (!player.isModelPartShown(PlayerModelPart.CAPE)) {
                    return;
                }
            } else {
                name = TextFormatting.stripFormatting(name);
            }
            if (name != null && ("Dinnerbone".equals(name) || "Grumm".equalsIgnoreCase(name))) {
                poseStack.translate(0, animatable.getBbHeight() + 0.1f, 0);
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180f));
            }
        }
    }

    protected boolean isVisible(T animatable) {
        return !animatable.isInvisible();
    }

    protected float getDeathMaxRotation(T animatable) {
        return 90f;
    }

    @Override
    public boolean shouldShowName(T animatable) {
        double nameRenderDistance = animatable.isDiscrete() ? 32d : 64d;
        if (this.entityRenderDispatcher.distanceToSqr(animatable) >= nameRenderDistance * nameRenderDistance) {
            return false;
        }
        return animatable == this.entityRenderDispatcher.crosshairPickEntity && animatable.hasCustomName() && Minecraft.renderNames();
    }

    protected float getSwingProgress(T animatable, float partialTick) {
        return animatable.getAttackAnim(partialTick);
    }

    protected float getSwingMotionAniMathHelperreshold() {
        return 0.15f;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return this.modelProvider.getTextureLocation(animatable);
    }

    public final boolean addLayer(GeoLayerRenderer<T> layer) {
        return this.layerRenderers.add(layer);
    }

    public <E extends Entity> void renderLeash(T entity, float partialTicks, MatrixStack poseStack,
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
            GeoEntityRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u,
                    false);
        }
        for (u = 24; u >= 0; --u) {
            GeoEntityRenderer.renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.0f, o, p, u,
                    true);
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
