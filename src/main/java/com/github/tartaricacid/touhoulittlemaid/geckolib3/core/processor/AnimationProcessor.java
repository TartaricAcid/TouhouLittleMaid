package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.AnimatableEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimationQueue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.AnimationContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.IForeignVariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.VariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util.StringPool;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneTopLevelSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.RateLimiter;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExpressionEvaluator;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AnimationProcessor<T extends AnimatableEntity<?>> {
    private static final int ROAMING_STRUCT_NAME = StringPool.computeIfAbsent("roaming");

    private final ReferenceArrayList<BoneTopLevelSnapshot> modelRendererList = new ReferenceArrayList<>();
    private final Object2ReferenceOpenHashMap<String, BoneTopLevelSnapshot> modelRendererMap = new Object2ReferenceOpenHashMap<>();
    private final VariableStorage animationStorage = new VariableStorage();
    private final Random random = new Random();
    private final ConcurrentLinkedQueue<Pair<IValue, Consumer<String>>> pendingValues = new ConcurrentLinkedQueue<>();
    private final RateLimiter rateLimiter = new RateLimiter(Minecraft.getInstance().getWindow().getRefreshRate());
    private final T animatable;

    private List<IValue> initializationValues;
    private List<IValue> preAnimationValues;

    private boolean rendererDirty = false;
    public boolean reloadAnimations = false;

    public AnimationProcessor(T animatable) {
        this.animatable = animatable;
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean tickAnimation(double seekTime, AnimationEvent<T> event, AnimationContext<?> ctx) {
        var shouldUpdate = rateLimiter.request((float) (seekTime / 20));

        ctx.setStorage(this.animationStorage);
        ctx.setRandom(this.random);
        ExpressionEvaluator<AnimationContext<?>> evaluator = ExpressionEvaluator.evaluator(ctx);
        preProcess(evaluator);

        // InstancedAnimationFactory 仅保有一个 AnimationData 实例，与传入的 uniqueID 无关
        AnimationData manager = this.animatable.getAnimationData();
        for (AnimationController<T> controller : manager.getAnimationControllers()) {
            if (reloadAnimations) {
                controller.markNeedsReload();
                controller.getBoneAnimationQueues().clear();
            }
            controller.isJustStarting = manager.isFirstTick;
            // 将当前控制器设置为动画测试事件
            event.setController(controller);
            // 处理动画并向点队列添加新值
            controller.process(seekTime, event, evaluator, modelRendererList, false, rendererDirty, shouldUpdate);
            boolean isParallelController = controller.getName().startsWith("parallel_");
            // 遍历每个骨骼，并对属性进行插值计算
            for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues()) {
                BoneTopLevelSnapshot snapshot = boneAnimation.topLevelSnapshot;
                BoneSnapshot initialSnapshot = snapshot.bone.getInitialSnapshot();
                PointData pointData = snapshot.cachedPointData;

                // 如果此骨骼有任何旋转值
                if (!boneAnimation.rotationQueue().isEmpty()) {
                    Vector3f scale = boneAnimation.rotationQueue().poll().getLerpPoint(evaluator);
                    pointData.rotationValueX += scale.x();
                    pointData.rotationValueY += scale.y();
                    pointData.rotationValueZ += scale.z();
                    if (isParallelController) {
                        snapshot.rotationValueX = pointData.rotationValueX + initialSnapshot.rotationValueX;
                        snapshot.rotationValueY = pointData.rotationValueY + initialSnapshot.rotationValueY;
                        snapshot.rotationValueZ = pointData.rotationValueZ + initialSnapshot.rotationValueZ;
                    } else {
                        snapshot.rotationValueX = scale.x() + initialSnapshot.rotationValueX;
                        snapshot.rotationValueY = scale.y() + initialSnapshot.rotationValueY;
                        snapshot.rotationValueZ = scale.z() + initialSnapshot.rotationValueZ;
                    }
                    snapshot.isCurrentlyRunningRotationAnimation = true;
                }

                // 如果此骨骼有任何位置值
                if (!boneAnimation.positionQueue().isEmpty()) {
                    Vector3f position = boneAnimation.positionQueue().poll().getLerpPoint(evaluator);
                    snapshot.positionOffsetX = position.x();
                    snapshot.positionOffsetY = position.y();
                    snapshot.positionOffsetZ = position.z();
                    snapshot.isCurrentlyRunningPositionAnimation = true;
                }

                // 如果此骨骼有任何缩放点
                if (!boneAnimation.scaleQueue().isEmpty()) {
                    Vector3f scale = boneAnimation.scaleQueue().poll().getLerpPoint(evaluator);
                    snapshot.scaleValueX = scale.x();
                    snapshot.scaleValueY = scale.y();
                    snapshot.scaleValueZ = scale.z();
                    snapshot.isCurrentlyRunningScaleAnimation = true;
                }
            }
        }

        this.rendererDirty = false;
        this.reloadAnimations = false;

        // 追踪哪些骨骼应用了动画，并最终将没有动画的骨骼设置为默认值
        final double resetTickLength = manager.getResetSpeed();
        for (BoneTopLevelSnapshot topLevelSnapshot : modelRendererList) {
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

        postProcess(evaluator);
        return true;
    }

    @Nullable
    public IBone getBone(String boneName) {
        BoneTopLevelSnapshot renderer = modelRendererMap.get(boneName);
        return renderer != null ? renderer.bone : null;
    }

    public void registerModelRenderer(Map<String, IBone> boneMap) {
        this.modelRendererMap.clear();
        this.modelRendererList.clear();
        this.modelRendererList.ensureCapacity(boneMap.size());
        for (Map.Entry<String, IBone> entry : boneMap.entrySet()) {
            BoneTopLevelSnapshot renderer = new BoneTopLevelSnapshot(entry.getValue());
            this.modelRendererMap.put(entry.getKey(), renderer);
            this.modelRendererList.add(renderer);
        }
        this.animationStorage.initialize(null);
        this.rendererDirty = true;
    }

    public boolean isModelRendererEmpty() {
        return modelRendererList.isEmpty();
    }

    public void preAnimationSetup(AnimatableEntity animatable, double seekTime) {
    }

    private void preProcess(ExpressionEvaluator<AnimationContext<?>> evaluator) {
        if (rendererDirty && initializationValues != null) {
            for (IValue value : initializationValues) {
                value.evalAsDouble(evaluator);
            }
            initializationValues = null;
        }
        if (preAnimationValues != null) {
            for (IValue value : preAnimationValues) {
                value.evalAsDouble(evaluator);
            }
        }
    }

    private void postProcess(ExpressionEvaluator<AnimationContext<?>> evaluator) {
        while (!pendingValues.isEmpty()) {
            Pair<IValue, Consumer<String>> pair = pendingValues.poll();
            String result;
            try {
                var ret = pair.getFirst().evalUnsafe(evaluator);
                if (ret == null) {
                    result = "null";
                } else if (ret instanceof String) {
                    result = "'" + ret + "'";
                } else {
                    result = ret.toString();
                }
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            if (pair.getSecond() != null) {
                pair.getSecond().accept(result);
            }
        }
    }

    public void execute(IValue value, @Nullable Consumer<String> resultConsumer) {
        pendingValues.add(Pair.of(value, resultConsumer));
    }

    public IForeignVariableStorage getPublicVariableStorage() {
        return this.animationStorage;
    }
}