package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Color;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.GeoModelProvider;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.EModelRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.IRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface IGeoRenderer<T> {
    String GLOW_PREFIX = "ysmGlow";

    IRenderTypeBuffer getCurrentRTB();

    default void setCurrentRTB(IRenderTypeBuffer bufferSource) {
    }

    GeoModelProvider getGeoModelProvider();

    ResourceLocation getTextureLocation(T animatable);

    @Nullable
    default GeoModel getGeoModel() {
        return null;
    }

    default void render(GeoModel model, T animatable, float partialTick, RenderType type, MatrixStack poseStack,
                        @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight,
                        int packedOverlay, float red, float green, float blue, float alpha) {
        setCurrentRTB(bufferSource);
        renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight,
                packedOverlay, red, green, blue, alpha);
        if (bufferSource != null) {
            buffer = bufferSource.getBuffer(type);
        }
        renderLate(animatable, poseStack, partialTick, bufferSource, buffer, packedLight,
                packedOverlay, red, green, blue, alpha);
        // 渲染所有根骨骼
        for (GeoBone group : model.topLevelBones) {
            renderRecursively(group, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                    alpha);
        }
        // 由于此时我们至少渲染了一次，因此让我们将循环设置为重复
        setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);
    }

    default void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        int cubePackedLight = packedLight;
        if (bone.getName().startsWith(GLOW_PREFIX)) {
            cubePackedLight = LightTexture.pack(15, 15);
        }
        poseStack.pushPose();
        RenderUtils.prepMatrixForBone(poseStack, bone);
        renderCubesOfBone(bone, poseStack, buffer, cubePackedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    default void renderCubesOfBone(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden()) {
            return;
        }
        for (GeoCube cube : bone.childCubes) {
            if (!bone.cubesAreHidden()) {
                poseStack.pushPose();
                renderCube(cube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                poseStack.popPose();
            }
        }
    }

    default void renderChildBones(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.childBonesAreHiddenToo()) {
            return;
        }
        for (GeoBone childBone : bone.childBones) {
            renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    default void renderCube(GeoCube cube, MatrixStack poseStack, IVertexBuilder buffer, int packedLight,
                            int packedOverlay, float red, float green, float blue, float alpha) {
        RenderUtils.translateToPivotPoint(poseStack, cube);
        RenderUtils.rotateMatrixAroundCube(poseStack, cube);
        RenderUtils.translateAwayFromPivotPoint(poseStack, cube);
        Matrix3f normalisedPoseState = poseStack.last().normal();
        Matrix4f poseState = poseStack.last().pose();
        for (GeoQuad quad : cube.quads) {
            if (quad == null) {
                continue;
            }
            Vector3f normal = quad.normal.copy();
            normal.transform(normalisedPoseState);
            if ((cube.size.y() == 0 || cube.size.z() == 0) && normal.x() < 0) {
                normal.mul(-1, 1, 1);
            }
            if ((cube.size.x() == 0 || cube.size.z() == 0) && normal.y() < 0) {
                normal.mul(1, -1, 1);
            }
            if ((cube.size.x() == 0 || cube.size.y() == 0) && normal.z() < 0) {
                normal.mul(1, 1, -1);
            }
            createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    default void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, IVertexBuilder buffer,
                                      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (GeoVertex vertex : quad.vertices) {
            Vector4f vector4f = new Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1);
            vector4f.transform(poseState);
            buffer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, vertex.textureU,
                    vertex.textureV, packedOverlay, packedLight, normal.x(), normal.y(), normal.z());
        }
    }

    default void renderEarly(T animatable, MatrixStack poseStack, float partialTick,
                             @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight,
                             int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (getCurrentModelRenderCycle() == EModelRenderCycle.INITIAL) {
            float width = getWidthScale(animatable);
            float height = getHeightScale(animatable);
            poseStack.scale(width, height, width);
        }
    }

    default void renderLate(T animatable, MatrixStack poseStack, float partialTick, IRenderTypeBuffer bufferSource,
                            IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                            float alpha) {
    }

    default RenderType getRenderType(T animatable, float partialTick, MatrixStack poseStack,
                                     @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight,
                                     ResourceLocation texture) {
        return RenderType.entityCutout(texture);
    }

    default Color getRenderColor(T animatable, float partialTick, MatrixStack poseStack,
                                 @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight) {
        return Color.WHITE;
    }

    default int getInstanceId(T animatable) {
        return animatable.hashCode();
    }

    @Nonnull
    default IRenderCycle getCurrentModelRenderCycle() {
        return EModelRenderCycle.INITIAL;
    }

    default void setCurrentModelRenderCycle(IRenderCycle cycle) {
    }

    default float getWidthScale(T animatable) {
        return 1F;
    }

    default float getHeightScale(T entity) {
        return 1F;
    }
}
