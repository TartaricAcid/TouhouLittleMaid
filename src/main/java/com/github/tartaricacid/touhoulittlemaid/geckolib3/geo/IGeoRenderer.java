package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.SodiumCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Color;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoMesh;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.EModelRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.IRenderCycle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
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
    Vector3f C000 = new Vector3f();
    Vector3f C100 = new Vector3f();
    Vector3f C110 = new Vector3f();
    Vector3f C010 = new Vector3f();
    Vector3f C001 = new Vector3f();
    Vector3f C101 = new Vector3f();
    Vector3f C111 = new Vector3f();
    Vector3f C011 = new Vector3f();
    Vector3f dx = new Vector3f();
    Vector3f dy = new Vector3f();
    Vector3f dz = new Vector3f();
    Vector3f nx = new Vector3f();
    Vector3f ny = new Vector3f();
    Vector3f nz = new Vector3f();

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
        if (!SodiumCompat.sodiumRenderCubesOfBone(bone, poseStack, buffer, cubePackedLight, packedOverlay, red, green, blue, alpha)) {
            renderCubesOfBone(bone, poseStack, buffer, cubePackedLight, packedOverlay, red, green, blue, alpha);
        }
        renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    default void renderCubesOfBone(AnimatedGeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden()) {
            return;
        }
        if (bone.cubesAreHidden()) {
            return;
        }

        GeoMesh mesh = bone.geoBone().cubes();

        for (int i = 0; i < mesh.getCubeCount(); i++) {
            Matrix4f pose = poseStack.last().pose();
            mesh.position(i).mulPosition(pose, C000);
            mesh.dx(i).mulDirection(pose, dx);
            mesh.dy(i).mulDirection(pose, dy);
            mesh.dz(i).mulDirection(pose, dz);

            C000.add(dx, C100);
            C100.add(dy, C110);
            C000.add(dy, C010);
            C000.add(dz, C001);
            C100.add(dz, C101);
            C110.add(dz, C111);
            C010.add(dz, C011);

            dx.cross(dy, nz).normalize();
            dy.cross(dz, nx).normalize();
            dz.cross(dx, ny).normalize();

            int faces = mesh.faces(i);
            boolean mirrored = (faces & 0b1000000) != 0;
            if (RenderSystem.getModelViewMatrix().m32() != 0) {
                Matrix3f normal = poseStack.last().normal();
                mesh.dx(i).cross(mesh.dy(i), nz);
                mesh.dy(i).cross(mesh.dz(i), nx);
                mesh.dz(i).cross(mesh.dx(i), ny);
                nx.mul(normal).normalize();
                ny.mul(normal).normalize();
                nz.mul(normal).normalize();
            }

            if (mirrored) {
                nx.mul(-1);
                ny.mul(-1);
                nz.mul(-1);
            }

            if ((faces & 0b000001) != 0) // DOWN
            {
                buffer.vertex(C101.x, C101.y, C101.z, red, green, blue, alpha, mesh.downU0(i), mesh.downV1(i),
                        packedOverlay, packedLight, -ny.x, -ny.y, -ny.z);
                buffer.vertex(C001.x, C001.y, C001.z, red, green, blue, alpha, mesh.downU1(i), mesh.downV1(i),
                        packedOverlay, packedLight, -ny.x, -ny.y, -ny.z);
                buffer.vertex(C000.x, C000.y, C000.z, red, green, blue, alpha, mesh.downU1(i), mesh.downV0(i),
                        packedOverlay, packedLight, -ny.x, -ny.y, -ny.z);
                buffer.vertex(C100.x, C100.y, C100.z, red, green, blue, alpha, mesh.downU0(i), mesh.downV0(i),
                        packedOverlay, packedLight, -ny.x, -ny.y, -ny.z);
            }
            if ((faces & 0b000010) != 0) // UP
            {
                buffer.vertex(C110.x, C110.y, C110.z, red, green, blue, alpha, mesh.upU0(i), mesh.upV1(i),
                        packedOverlay, packedLight, ny.x, ny.y, ny.z);
                buffer.vertex(C010.x, C010.y, C010.z, red, green, blue, alpha, mesh.upU1(i), mesh.upV1(i),
                        packedOverlay, packedLight, ny.x, ny.y, ny.z);
                buffer.vertex(C011.x, C011.y, C011.z, red, green, blue, alpha, mesh.upU1(i), mesh.upV0(i),
                        packedOverlay, packedLight, ny.x, ny.y, ny.z);
                buffer.vertex(C111.x, C111.y, C111.z, red, green, blue, alpha, mesh.upU0(i), mesh.upV0(i),
                        packedOverlay, packedLight, ny.x, ny.y, ny.z);
            }
            if ((faces & 0b000100) != 0) // NORTH
            {
                buffer.vertex(C100.x, C100.y, C100.z, red, green, blue, alpha, mesh.northU0(i), mesh.northV1(i),
                        packedOverlay, packedLight, -nz.x, -nz.y, -nz.z);
                buffer.vertex(C000.x, C000.y, C000.z, red, green, blue, alpha, mesh.northU1(i), mesh.northV1(i),
                        packedOverlay, packedLight, -nz.x, -nz.y, -nz.z);
                buffer.vertex(C010.x, C010.y, C010.z, red, green, blue, alpha, mesh.northU1(i), mesh.northV0(i),
                        packedOverlay, packedLight, -nz.x, -nz.y, -nz.z);
                buffer.vertex(C110.x, C110.y, C110.z, red, green, blue, alpha, mesh.northU0(i), mesh.northV0(i),
                        packedOverlay, packedLight, -nz.x, -nz.y, -nz.z);
            }
            if ((faces & 0b001000) != 0) // SOUTH
            {
                buffer.vertex(C001.x, C001.y, C001.z, red, green, blue, alpha, mesh.southU0(i), mesh.southV1(i),
                        packedOverlay, packedLight, nz.x, nz.y, nz.z);
                buffer.vertex(C101.x, C101.y, C101.z, red, green, blue, alpha, mesh.southU1(i), mesh.southV1(i),
                        packedOverlay, packedLight, nz.x, nz.y, nz.z);
                buffer.vertex(C111.x, C111.y, C111.z, red, green, blue, alpha, mesh.southU1(i), mesh.southV0(i),
                        packedOverlay, packedLight, nz.x, nz.y, nz.z);
                buffer.vertex(C011.x, C011.y, C011.z, red, green, blue, alpha, mesh.southU0(i), mesh.southV0(i),
                        packedOverlay, packedLight, nz.x, nz.y, nz.z);
            }
            if ((faces & 0b010000) != 0) // WEST
            {
                buffer.vertex(C000.x, C000.y, C000.z, red, green, blue, alpha, mesh.westU0(i), mesh.westV1(i),
                        packedOverlay, packedLight, -nx.x, -nx.y, -nx.z);
                buffer.vertex(C001.x, C001.y, C001.z, red, green, blue, alpha, mesh.westU1(i), mesh.westV1(i),
                        packedOverlay, packedLight, -nx.x, -nx.y, -nx.z);
                buffer.vertex(C011.x, C011.y, C011.z, red, green, blue, alpha, mesh.westU1(i), mesh.westV0(i),
                        packedOverlay, packedLight, -nx.x, -nx.y, -nx.z);
                buffer.vertex(C010.x, C010.y, C010.z, red, green, blue, alpha, mesh.westU0(i), mesh.westV0(i),
                        packedOverlay, packedLight, -nx.x, -nx.y, -nx.z);
            }
            if ((faces & 0b100000) != 0) // EAST
            {
                buffer.vertex(C101.x, C101.y, C101.z, red, green, blue, alpha, mesh.eastU0(i), mesh.eastV1(i),
                        packedOverlay, packedLight, nx.x, nx.y, nx.z);
                buffer.vertex(C100.x, C100.y, C100.z, red, green, blue, alpha, mesh.eastU1(i), mesh.eastV1(i),
                        packedOverlay, packedLight, nx.x, nx.y, nx.z);
                buffer.vertex(C110.x, C110.y, C110.z, red, green, blue, alpha, mesh.eastU1(i), mesh.eastV0(i),
                        packedOverlay, packedLight, nx.x, nx.y, nx.z);
                buffer.vertex(C111.x, C111.y, C111.z, red, green, blue, alpha, mesh.eastU0(i), mesh.eastV0(i),
                        packedOverlay, packedLight, nx.x, nx.y, nx.z);
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
        return RenderType.entityCutoutNoCull(texture);
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