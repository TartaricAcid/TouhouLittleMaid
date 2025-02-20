package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.ILocationBone;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public final class RenderUtils {
    public static void translateMatrixToBone(PoseStack poseStack, ILocationBone bone) {
        poseStack.translate(-bone.getPositionX() / 16f, bone.getPositionY() / 16f, bone.getPositionZ() / 16f);
    }

    public static void rotateMatrixAroundBone(PoseStack poseStack, ILocationBone bone) {
        if (bone.getRotationZ() != 0.0F || bone.getRotationY() != 0.0F || bone.getRotationX() != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotateZYX(bone.getRotationZ(), bone.getRotationY(), bone.getRotationX()));
        }
    }

    public static void scaleMatrixForBone(PoseStack poseStack, ILocationBone bone) {
        poseStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void translateToPivotPoint(PoseStack poseStack, ILocationBone bone) {
        poseStack.translate(bone.getPivotX() / 16f, bone.getPivotY() / 16f, bone.getPivotZ() / 16f);
    }

    public static void translateAwayFromPivotPoint(PoseStack poseStack, ILocationBone bone) {
        poseStack.translate(-bone.getPivotX() / 16f, -bone.getPivotY() / 16f, -bone.getPivotZ() / 16f);
    }

    public static void translateAndRotateMatrixForBone(PoseStack poseStack, ILocationBone bone) {
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
    }

    public static void prepMatrixForBone(PoseStack poseStack, ILocationBone bone) {
        translateMatrixToBone(poseStack, bone);
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
        scaleMatrixForBone(poseStack, bone);
        translateAwayFromPivotPoint(poseStack, bone);
    }

    public static Matrix4f invertAndMultiplyMatrices(Matrix4f baseMatrix, Matrix4f inputMatrix) {
        inputMatrix = new Matrix4f(inputMatrix);
        inputMatrix.invert();
        inputMatrix.mul(baseMatrix);
        return inputMatrix;
    }

    public static void prepMatrixForLocator(PoseStack poseStack, List<? extends ILocationBone> locatorHierarchy) {
        if (locatorHierarchy == null || locatorHierarchy.isEmpty()) {
            return;
        }
        for (int i = 0; i < locatorHierarchy.size() - 1; i++) {
            RenderUtils.prepMatrixForBone(poseStack, locatorHierarchy.get(i));
        }
        ILocationBone lastBone = locatorHierarchy.get(locatorHierarchy.size() - 1);
        RenderUtils.translateMatrixToBone(poseStack, lastBone);
        RenderUtils.translateToPivotPoint(poseStack, lastBone);
        RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
        RenderUtils.scaleMatrixForBone(poseStack, lastBone);
    }
}
