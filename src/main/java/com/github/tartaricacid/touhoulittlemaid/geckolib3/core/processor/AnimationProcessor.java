package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatableModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.AnimationPoint;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimationQueue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.DirtyTracker;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AnimationProcessor<T extends IAnimatable> {
    private final List<IBone> modelRendererList = new ObjectArrayList<>();
    private final IntSet animatedEntities = new IntOpenHashSet();
    private final IAnimatableModel animatedModel;
    public boolean reloadAnimations = false;
    private double lastTickValue = -1;

    public AnimationProcessor(IAnimatableModel animatedModel) {
        this.animatedModel = animatedModel;
    }

    public void tickAnimation(IAnimatable entity, int uniqueID, double seekTime, AnimationEvent<T> event, MolangParser parser, boolean crashWhenCantFindBone) {
        if (seekTime != lastTickValue) {
            animatedEntities.clear();
        } else if (animatedEntities.contains(uniqueID)) {
            // 如果实体已经在此 tick 上播放了
            return;
        }

        lastTickValue = seekTime;
        animatedEntities.add(uniqueID);
        // 每一个动画都有自己的动画数据（AnimationData）
        // 这样多个动画就能互相独立了
        AnimationData manager = entity.getFactory().getOrCreateAnimationData(uniqueID);
        // 追踪哪些骨骼应用了动画，并最终将没有动画的骨骼设置为默认值
        Map<String, DirtyTracker> modelTracker = createNewDirtyTracker();
        // 存储每个骨骼的 rotation/position/scale
        updateBoneSnapshots(manager.getBoneSnapshotCollection());
        Map<String, Pair<IBone, BoneSnapshot>> boneSnapshots = manager.getBoneSnapshotCollection();
        HashMap<String, PointData> pointDataGroup = Maps.newHashMap();
        for (AnimationController<T> controller : manager.getAnimationControllers().values()) {
            if (reloadAnimations) {
                controller.markNeedsReload();
                controller.getBoneAnimationQueues().clear();
            }
            controller.isJustStarting = manager.isFirstTick;
            // 将当前控制器设置为动画测试事件
            event.setController(controller);
            // 处理动画并向点队列添加新值
            controller.process(seekTime, event, modelRendererList, boneSnapshots, parser, crashWhenCantFindBone);
            // 遍历每个骨骼，并对属性进行插值计算
            for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
                IBone bone = boneAnimation.bone();
                BoneSnapshot snapshot = boneSnapshots.get(bone.getName()).getRight();
                BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                pointDataGroup.putIfAbsent(bone.getName(), new PointData());
                PointData pointData = pointDataGroup.get(bone.getName());

                AnimationPoint rXPoint = boneAnimation.rotationXQueue().poll();
                AnimationPoint rYPoint = boneAnimation.rotationYQueue().poll();
                AnimationPoint rZPoint = boneAnimation.rotationZQueue().poll();

                AnimationPoint pXPoint = boneAnimation.positionXQueue().poll();
                AnimationPoint pYPoint = boneAnimation.positionYQueue().poll();
                AnimationPoint pZPoint = boneAnimation.positionZQueue().poll();

                AnimationPoint sXPoint = boneAnimation.scaleXQueue().poll();
                AnimationPoint sYPoint = boneAnimation.scaleYQueue().poll();
                AnimationPoint sZPoint = boneAnimation.scaleZQueue().poll();


                DirtyTracker dirtyTracker = modelTracker.get(bone.getName());
                if (dirtyTracker == null) {
                    continue;
                }

                // 如果此骨骼有任何旋转值
                if (rXPoint != null && rYPoint != null && rZPoint != null) {
                    float valueX = MathUtil.lerpValues(rXPoint, controller.easingType, controller.customEasingMethod);
                    float valueY = MathUtil.lerpValues(rYPoint, controller.easingType, controller.customEasingMethod);
                    float valueZ = MathUtil.lerpValues(rZPoint, controller.easingType, controller.customEasingMethod);
                    pointData.rotationValueX += valueX;
                    pointData.rotationValueY += valueY;
                    pointData.rotationValueZ += valueZ;
                    if (controller.getName().startsWith("parallel_")) {
                        bone.setRotationX(pointData.rotationValueX + initialSnapshot.rotationValueX);
                        bone.setRotationY(pointData.rotationValueY + initialSnapshot.rotationValueY);
                        bone.setRotationZ(pointData.rotationValueZ + initialSnapshot.rotationValueZ);
                    } else {
                        bone.setRotationX(valueX + initialSnapshot.rotationValueX);
                        bone.setRotationY(valueY + initialSnapshot.rotationValueY);
                        bone.setRotationZ(valueZ + initialSnapshot.rotationValueZ);
                    }
                    snapshot.rotationValueX = bone.getRotationX();
                    snapshot.rotationValueY = bone.getRotationY();
                    snapshot.rotationValueZ = bone.getRotationZ();
                    snapshot.isCurrentlyRunningRotationAnimation = true;
                    dirtyTracker.hasRotationChanged = true;
                }

                // 如果此骨骼有任何位置值
                if (pXPoint != null && pYPoint != null && pZPoint != null) {
                    bone.setPositionX(
                            MathUtil.lerpValues(pXPoint, controller.easingType, controller.customEasingMethod));
                    bone.setPositionY(
                            MathUtil.lerpValues(pYPoint, controller.easingType, controller.customEasingMethod));
                    bone.setPositionZ(
                            MathUtil.lerpValues(pZPoint, controller.easingType, controller.customEasingMethod));
                    snapshot.positionOffsetX = bone.getPositionX();
                    snapshot.positionOffsetY = bone.getPositionY();
                    snapshot.positionOffsetZ = bone.getPositionZ();
                    snapshot.isCurrentlyRunningPositionAnimation = true;
                    dirtyTracker.hasPositionChanged = true;
                }

                // 如果此骨骼有任何缩放点
                if (sXPoint != null && sYPoint != null && sZPoint != null) {
                    bone.setScaleX(MathUtil.lerpValues(sXPoint, controller.easingType, controller.customEasingMethod));
                    bone.setScaleY(MathUtil.lerpValues(sYPoint, controller.easingType, controller.customEasingMethod));
                    bone.setScaleZ(MathUtil.lerpValues(sZPoint, controller.easingType, controller.customEasingMethod));
                    snapshot.scaleValueX = bone.getScaleX();
                    snapshot.scaleValueY = bone.getScaleY();
                    snapshot.scaleValueZ = bone.getScaleZ();
                    snapshot.isCurrentlyRunningScaleAnimation = true;
                    dirtyTracker.hasScaleChanged = true;
                }
            }
        }
        this.reloadAnimations = false;

        double resetTickLength = manager.getResetSpeed();
        for (Map.Entry<String, DirtyTracker> tracker : modelTracker.entrySet()) {
            IBone model = tracker.getValue().model;
            BoneSnapshot initialSnapshot = model.getInitialSnapshot();
            BoneSnapshot saveSnapshot = boneSnapshots.get(tracker.getKey()).getRight();
            if (saveSnapshot == null) {
                if (crashWhenCantFindBone) {
                    throw new RuntimeException("Could not find save snapshot for bone: " + tracker.getValue().model.getName()
                            + ". Please don't add bones that are used in an animation at runtime.");
                } else {
                    continue;
                }
            }

            if (!tracker.getValue().hasRotationChanged) {
                if (saveSnapshot.isCurrentlyRunningRotationAnimation) {
                    // FIXME: 2023/7/12 莫名其妙修好了旋转 bug，原因未知
                    saveSnapshot.mostRecentResetRotationTick = 0;
                    saveSnapshot.isCurrentlyRunningRotationAnimation = false;
                }
                double percentageReset = Math.min((seekTime - saveSnapshot.mostRecentResetRotationTick) / resetTickLength, 1);
                model.setRotationX(MathUtil.lerpValues(percentageReset, saveSnapshot.rotationValueX,
                        initialSnapshot.rotationValueX));
                model.setRotationY(MathUtil.lerpValues(percentageReset, saveSnapshot.rotationValueY,
                        initialSnapshot.rotationValueY));
                model.setRotationZ(MathUtil.lerpValues(percentageReset, saveSnapshot.rotationValueZ,
                        initialSnapshot.rotationValueZ));
                if (percentageReset >= 1) {
                    saveSnapshot.rotationValueX = model.getRotationX();
                    saveSnapshot.rotationValueY = model.getRotationY();
                    saveSnapshot.rotationValueZ = model.getRotationZ();
                }
            }
            if (!tracker.getValue().hasPositionChanged) {
                if (saveSnapshot.isCurrentlyRunningPositionAnimation) {
                    saveSnapshot.mostRecentResetPositionTick = (float) seekTime;
                    saveSnapshot.isCurrentlyRunningPositionAnimation = false;
                }
                double percentageReset = Math.min((seekTime - saveSnapshot.mostRecentResetPositionTick) / resetTickLength, 1);
                model.setPositionX(MathUtil.lerpValues(percentageReset, saveSnapshot.positionOffsetX,
                        initialSnapshot.positionOffsetX));
                model.setPositionY(MathUtil.lerpValues(percentageReset, saveSnapshot.positionOffsetY,
                        initialSnapshot.positionOffsetY));
                model.setPositionZ(MathUtil.lerpValues(percentageReset, saveSnapshot.positionOffsetZ,
                        initialSnapshot.positionOffsetZ));
                if (percentageReset >= 1) {
                    saveSnapshot.positionOffsetX = model.getPositionX();
                    saveSnapshot.positionOffsetY = model.getPositionY();
                    saveSnapshot.positionOffsetZ = model.getPositionZ();
                }
            }
            if (!tracker.getValue().hasScaleChanged) {
                if (saveSnapshot.isCurrentlyRunningScaleAnimation) {
                    saveSnapshot.mostRecentResetScaleTick = (float) seekTime;
                    saveSnapshot.isCurrentlyRunningScaleAnimation = false;
                }
                double percentageReset = Math.min((seekTime - saveSnapshot.mostRecentResetScaleTick) / resetTickLength, 1);
                model.setScaleX(
                        MathUtil.lerpValues(percentageReset, saveSnapshot.scaleValueX, initialSnapshot.scaleValueX));
                model.setScaleY(
                        MathUtil.lerpValues(percentageReset, saveSnapshot.scaleValueY, initialSnapshot.scaleValueY));
                model.setScaleZ(
                        MathUtil.lerpValues(percentageReset, saveSnapshot.scaleValueZ, initialSnapshot.scaleValueZ));
                if (percentageReset >= 1) {
                    saveSnapshot.scaleValueX = model.getScaleX();
                    saveSnapshot.scaleValueY = model.getScaleY();
                    saveSnapshot.scaleValueZ = model.getScaleZ();
                }
            }
        }
        manager.isFirstTick = false;
    }

    private Map<String, DirtyTracker> createNewDirtyTracker() {
        Map<String, DirtyTracker> tracker = new Object2ObjectOpenHashMap<>();
        for (IBone bone : modelRendererList) {
            tracker.put(bone.getName(), new DirtyTracker(false, false, false, bone));
        }
        return tracker;
    }

    private void updateBoneSnapshots(Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection) {
        for (IBone bone : modelRendererList) {
            if (!boneSnapshotCollection.containsKey(bone.getName())) {
                boneSnapshotCollection.put(bone.getName(), Pair.of(bone, new BoneSnapshot(bone.getInitialSnapshot())));
            }
        }
    }

    public IBone getBone(String boneName) {
        for (IBone bone : this.modelRendererList) {
            if (bone.getName().equals(boneName)) {
                return bone;
            }
        }
        return null;
    }

    public void registerModelRenderer(IBone modelRenderer) {
        modelRenderer.saveInitialSnapshot();
        modelRendererList.add(modelRenderer);
    }

    public void clearModelRendererList() {
        this.modelRendererList.clear();
    }

    public List<IBone> getModelRendererList() {
        return modelRendererList;
    }

    public void preAnimationSetup(IAnimatable animatable, double seekTime) {
        this.animatedModel.setMolangQueries(animatable, seekTime);
    }
}
