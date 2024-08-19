package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Color;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoCube;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoQuad;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoVertex;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.EModelRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.IRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IGeoRenderer<T> {
    MultiBufferSource getCurrentRTB();

    default void setCurrentRTB(MultiBufferSource bufferSource) {
    }

    ResourceLocation getTextureLocation(T animatable);

    default void render(AnimatedGeoModel model, T animatable, float partialTick, RenderType type, PoseStack poseStack,
                        @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
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
        for (AnimatedGeoBone group : model.topLevelBones()) {
            renderRecursively(group, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                    alpha);
        }
        // 由于此时我们至少渲染了一次，因此让我们将循环设置为重复
        setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);
    }

    default void renderRecursively(AnimatedGeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        int cubePackedLight = bone.geoBone().glow() ? LightTexture.pack(15, 15) : packedLight;
        if ((bone.getScaleX() == 0 ? 0 : 1) + (bone.getScaleY() == 0 ? 0 : 1) + (bone.getScaleZ() == 0 ? 0 : 1) < 2) {
            return;
        }
        poseStack.pushPose();
        RenderUtils.prepMatrixForBone(poseStack, bone);
        renderCubesOfBone(bone, poseStack, buffer, cubePackedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    default void renderCubesOfBone(AnimatedGeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden()) {
            return;
        }
        if (!bone.cubesAreHidden()) {
            for (GeoCube cube : bone.geoBone().cubes()) {
                renderCube(cube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }

    default void renderChildBones(AnimatedGeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.childBonesAreHiddenToo()) {
            return;
        }
        for (AnimatedGeoBone childBone : bone.children()) {
            renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    default void renderCube(GeoCube cube, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                            int packedOverlay, float red, float green, float blue, float alpha) {
        Matrix3f normalisedPoseState = poseStack.last().normal();
        Matrix4f poseState = poseStack.last().pose();
        for (GeoQuad quad : cube.quads) {
            if (quad == null) {
                continue;
            }
            Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal));
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

    default void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer,
                                      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        var position = new Vector3f();
        for (GeoVertex vertex : quad.vertices) {
            vertex.position.mulPosition(poseState, position);
            buffer.addVertex(position.x(), position.y(), position.z()).setColor(red, green, blue, alpha).setUv(vertex.textureU, vertex.textureV)
                    .setOverlay(packedOverlay).setLight(packedLight).setNormal(normal.x(), normal.y(), normal.z());
        }
    }

    default void renderEarly(T animatable, PoseStack poseStack, float partialTick,
                             @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
                             int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (getCurrentModelRenderCycle() == EModelRenderCycle.INITIAL) {
            float width = getWidthScale(animatable);
            float height = getHeightScale(animatable);
            poseStack.scale(width, height, width);
        }
    }

    default void renderLate(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                            float alpha) {
    }

    default RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
                                     @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
                                     ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    default Color getRenderColor(T animatable, float partialTick, PoseStack poseStack,
                                 @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        return Color.WHITE;
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
