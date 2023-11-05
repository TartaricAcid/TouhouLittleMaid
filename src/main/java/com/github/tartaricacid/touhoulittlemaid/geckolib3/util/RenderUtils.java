package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoCube;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public final class RenderUtils {
    public static void translateMatrixToBone(MatrixStack poseStack, GeoBone bone) {
        poseStack.translate(-bone.getPositionX() / 16f, bone.getPositionY() / 16f, bone.getPositionZ() / 16f);
    }

    public static void rotateMatrixAroundBone(MatrixStack poseStack, GeoBone bone) {
        if (bone.getRotationZ() != 0.0F) {
            poseStack.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
        }
        if (bone.getRotationY() != 0.0F) {
            poseStack.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
        }
        if (bone.getRotationX() != 0.0F) {
            poseStack.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
        }
    }

    public static void rotateMatrixAroundCube(MatrixStack poseStack, GeoCube cube) {
        Vector3f rotation = cube.rotation;
        poseStack.mulPose(new Quaternion(0, 0, rotation.z(), false));
        poseStack.mulPose(new Quaternion(0, rotation.y(), 0, false));
        poseStack.mulPose(new Quaternion(rotation.x(), 0, 0, false));
    }

    public static void scaleMatrixForBone(MatrixStack poseStack, GeoBone bone) {
        poseStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void translateToPivotPoint(MatrixStack poseStack, GeoCube cube) {
        Vector3f pivot = cube.pivot;
        poseStack.translate(pivot.x() / 16f, pivot.y() / 16f, pivot.z() / 16f);
    }

    public static void translateToPivotPoint(MatrixStack poseStack, GeoBone bone) {
        poseStack.translate(bone.rotationPointX / 16f, bone.rotationPointY / 16f, bone.rotationPointZ / 16f);
    }

    public static void translateAwayFromPivotPoint(MatrixStack poseStack, GeoCube cube) {
        Vector3f pivot = cube.pivot;
        poseStack.translate(-pivot.x() / 16f, -pivot.y() / 16f, -pivot.z() / 16f);
    }

    public static void translateAwayFromPivotPoint(MatrixStack poseStack, GeoBone bone) {
        poseStack.translate(-bone.rotationPointX / 16f, -bone.rotationPointY / 16f, -bone.rotationPointZ / 16f);
    }

    public static void translateAndRotateMatrixForBone(MatrixStack poseStack, GeoBone bone) {
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
    }

    public static void prepMatrixForBone(MatrixStack poseStack, GeoBone bone) {
        translateMatrixToBone(poseStack, bone);
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
        scaleMatrixForBone(poseStack, bone);
        translateAwayFromPivotPoint(poseStack, bone);
    }

    public static Matrix4f invertAndMultiplyMatrices(Matrix4f baseMatrix, Matrix4f inputMatrix) {
        inputMatrix = inputMatrix.copy();
        inputMatrix.invert();
        inputMatrix.multiply(baseMatrix);
        return inputMatrix;
    }
}
