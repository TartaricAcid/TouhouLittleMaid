package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.AnimatableEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.AnimationPoint;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimationQueue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneTopLevelSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AnimationProcessor<T extends AnimatableEntity> {
    private final Map<String, BoneTopLevelSnapshot> boneMap = new Object2ObjectOpenHashMap();
    private final T animatable;

    private boolean reloadAnimations = false;
    private boolean modelDirty = false;

    public AnimationProcessor(T animatable) {
        this.animatable = animatable;
    }

    public void tickAnimation(double seekTime, AnimationEvent event, MolangParser parser) {
        // 每一个动画都有自己的动画数据（AnimationData）
        // 这样多个动画就能互相独立了
        AnimationData manager = this.animatable.getAnimationData();
        for (AnimationController<?> controller : manager.getAnimationControllers().values()) {
            if (reloadAnimations) {
                controller.markNeedsReload();
                controller.getBoneAnimationQueues().clear();
            }
            controller.isJustStarting = manager.isFirstTick;
            // 将当前控制器设置为动画测试事件
            event.setController(controller);
            // 处理动画并向点队列添加新值
            controller.process(seekTime, event, this.boneMap, parser, this.modelDirty);
            boolean isParallelController = controller.getName().startsWith("parallel_");
            // 遍历每个骨骼，并对属性进行插值计算
            for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues()) {
                BoneTopLevelSnapshot snapshot = boneAnimation.topLevelSnapshot;
                BoneSnapshot initialSnapshot = snapshot.bone.getInitialSnapshot();
                PointData pointData = snapshot.cachedPointData;

                AnimationPoint rXPoint = boneAnimation.rotationXQueue().poll();
                AnimationPoint rYPoint = boneAnimation.rotationYQueue().poll();
                AnimationPoint rZPoint = boneAnimation.rotationZQueue().poll();
                // 如果此骨骼有任何旋转值
                if (rXPoint != null && rYPoint != null && rZPoint != null) {
                    float valueX = MathUtil.lerpValues(rXPoint, controller.easingType, controller.customEasingMethod);
                    float valueY = MathUtil.lerpValues(rYPoint, controller.easingType, controller.customEasingMethod);
                    float valueZ = MathUtil.lerpValues(rZPoint, controller.easingType, controller.customEasingMethod);
                    pointData.rotationValueX += valueX;
                    pointData.rotationValueY += valueY;
                    pointData.rotationValueZ += valueZ;
                    if (isParallelController) {
                        snapshot.rotationValueX = pointData.rotationValueX + initialSnapshot.rotationValueX;
                        snapshot.rotationValueY = pointData.rotationValueY + initialSnapshot.rotationValueY;
                        snapshot.rotationValueZ = pointData.rotationValueZ + initialSnapshot.rotationValueZ;
                    } else {
                        snapshot.rotationValueX = valueX + initialSnapshot.rotationValueX;
                        snapshot.rotationValueY = valueY + initialSnapshot.rotationValueY;
                        snapshot.rotationValueZ = valueZ + initialSnapshot.rotationValueZ;
                    }
                    snapshot.isCurrentlyRunningRotationAnimation = true;
                }

                AnimationPoint pXPoint = boneAnimation.positionXQueue().poll();
                AnimationPoint pYPoint = boneAnimation.positionYQueue().poll();
                AnimationPoint pZPoint = boneAnimation.positionZQueue().poll();
                // 如果此骨骼有任何位置值
                if (pXPoint != null && pYPoint != null && pZPoint != null) {
                    snapshot.positionOffsetX = MathUtil.lerpValues(pXPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.positionOffsetY = MathUtil.lerpValues(pYPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.positionOffsetZ = MathUtil.lerpValues(pZPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.isCurrentlyRunningPositionAnimation = true;
                }


                AnimationPoint sXPoint = boneAnimation.scaleXQueue().poll();
                AnimationPoint sYPoint = boneAnimation.scaleYQueue().poll();
                AnimationPoint sZPoint = boneAnimation.scaleZQueue().poll();
                // 如果此骨骼有任何缩放点
                if (sXPoint != null && sYPoint != null && sZPoint != null) {
                    snapshot.scaleValueX = MathUtil.lerpValues(sXPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.scaleValueY = MathUtil.lerpValues(sYPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.scaleValueZ = MathUtil.lerpValues(sZPoint, controller.easingType, controller.customEasingMethod);
                    snapshot.isCurrentlyRunningScaleAnimation = true;
                }
            }
        }

        this.modelDirty = false;
        this.reloadAnimations = false;

        // 追踪哪些骨骼应用了动画，并最终将没有动画的骨骼设置为默认值
        final double resetTickLength = manager.getResetSpeed();
        for (BoneTopLevelSnapshot topLevelSnapshot : this.boneMap.values()) {
            BoneSnapshot initialSnapshot = topLevelSnapshot.bone.getInitialSnapshot();

            if (!topLevelSnapshot.isCurrentlyRunningRotationAnimation) {
                double percentageReset = Math.min((seekTime - topLevelSnapshot.mostRecentResetRotationTick) / resetTickLength, 1);
                if (percentageReset >= 1) {
                    topLevelSnapshot.rotationValueX = MathUtil.lerpValues(percentageReset, topLevelSnapshot.rotationValueX,
                            initialSnapshot.rotationValueX);
                    topLevelSnapshot.rotationValueY = MathUtil.lerpValues(percentageReset, topLevelSnapshot.rotationValueY,
                            initialSnapshot.rotationValueY);
                    topLevelSnapshot.rotationValueZ = MathUtil.lerpValues(percentageReset, topLevelSnapshot.rotationValueZ,
                            initialSnapshot.rotationValueZ);
                }
            } else {
                // FIXME: 2023/7/12 莫名其妙修好了旋转 bug，原因未知
                topLevelSnapshot.mostRecentResetRotationTick = 0;
                topLevelSnapshot.isCurrentlyRunningRotationAnimation = false;
            }

            if (!topLevelSnapshot.isCurrentlyRunningPositionAnimation) {
                double percentageReset = Math.min((seekTime - topLevelSnapshot.mostRecentResetPositionTick) / resetTickLength, 1);
                if (percentageReset >= 1) {
                    topLevelSnapshot.positionOffsetX = MathUtil.lerpValues(percentageReset, topLevelSnapshot.positionOffsetX,
                            initialSnapshot.positionOffsetX);
                    topLevelSnapshot.positionOffsetY = MathUtil.lerpValues(percentageReset, topLevelSnapshot.positionOffsetY,
                            initialSnapshot.positionOffsetY);
                    topLevelSnapshot.positionOffsetZ = MathUtil.lerpValues(percentageReset, topLevelSnapshot.positionOffsetZ,
                            initialSnapshot.positionOffsetZ);
                }
            } else {
                topLevelSnapshot.mostRecentResetPositionTick = (float) seekTime;
                topLevelSnapshot.isCurrentlyRunningPositionAnimation = false;
            }

            if (!topLevelSnapshot.isCurrentlyRunningScaleAnimation) {
                double percentageReset = Math.min((seekTime - topLevelSnapshot.mostRecentResetScaleTick) / resetTickLength, 1);
                if (percentageReset >= 1) {
                    topLevelSnapshot.scaleValueX = MathUtil.lerpValues(percentageReset, topLevelSnapshot.scaleValueX, initialSnapshot.scaleValueX);
                    topLevelSnapshot.scaleValueY = MathUtil.lerpValues(percentageReset, topLevelSnapshot.scaleValueY, initialSnapshot.scaleValueY);
                    topLevelSnapshot.scaleValueZ = MathUtil.lerpValues(percentageReset, topLevelSnapshot.scaleValueZ, initialSnapshot.scaleValueZ);
                }
            } else {
                topLevelSnapshot.mostRecentResetScaleTick = (float) seekTime;
                topLevelSnapshot.isCurrentlyRunningScaleAnimation = false;
            }

            topLevelSnapshot.commit();
        }
        manager.isFirstTick = false;
    }

    public void updateModel(Collection<IBone> bones) {
        this.boneMap.clear();
        for (var bone : bones) {
            this.boneMap.put(bone.getName(), new BoneTopLevelSnapshot(bone));
        }
        modelDirty = true;
    }

    public boolean isModelEmpty() {
        return this.boneMap.isEmpty();
    }

    public void preAnimationSetup(double seekTime) {
        this.animatable.setMolangQueries(seekTime);
    }
}
