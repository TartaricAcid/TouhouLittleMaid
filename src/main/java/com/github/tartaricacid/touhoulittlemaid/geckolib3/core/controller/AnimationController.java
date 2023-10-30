/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing.EasingType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.CustomInstructionKeyframeEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.ParticleKeyFrameEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.SoundKeyframeEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.Axis;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnimationController<T extends IAnimatable> {
    private static final List<ModelFetcher<?>> modelFetchers = new ObjectArrayList<>();
    /**
     * 动画控制器名称
     */
    private final String name;
    private final HashMap<String, BoneAnimationQueue> boneAnimationQueues = new HashMap<>();
    private final HashMap<String, BoneSnapshot> boneSnapshots = new HashMap<>();
    private final Set<EventKeyFrame<?>> executedKeyFrames = new ObjectOpenHashSet<>();
    /**
     * 在动画之间过渡需要多长时间
     */
    public double transitionLengthTicks;
    public boolean isJustStarting = false;
    public double tickOffset;
    public Double2DoubleFunction customEasingMethod;
    public double animationSpeed = 1D;
    /**
     * 默认情况下，动画将使用关键帧的 EasingType <br>
     * 复写此数值将用于全局
     */
    public EasingType easingType = EasingType.NONE;
    /**
     * 实体对象
     */
    protected T animatable;
    /**
     * 动画谓词，每次触发前都会调用一次
     */
    protected IAnimationPredicate<T> animationPredicate;
    protected AnimationState animationState = AnimationState.STOPPED;
    protected Queue<Animation> animationQueue = new LinkedList<>();
    protected Animation currentAnimation;
    protected AnimationBuilder currentAnimationBuilder = new AnimationBuilder();
    public boolean shouldResetTick = false;
    protected boolean justStartedTransition = false;
    protected boolean needsAnimationReload = false;
    /**
     * 播放声音关键帧时触发的 Sound Listener
     */
    private ISoundListener<T> soundListener;
    /**
     * 播放粒子关键帧时触发的 Particle Listener
     */
    private IParticleListener<T> particleListener;
    /**
     * 播放指令关键帧时触发的 Instruction Listener
     */
    private ICustomInstructionListener<T> customInstructionListener;
    private boolean justStopped = false;

    /**
     * 实例化动画控制器，每个控制器同一时间只能播放一个动画 <br>
     * 你可以为一个实体附加多个动画控制器 <br>
     * 比如一个控制器控制实体大小，另一个控制移动，攻击等等
     *
     * @param animatable            实体
     * @param name                  动画控制器名称
     * @param transitionLengthTicks 动画过渡时间（tick）
     */
    public AnimationController(T animatable, String name, float transitionLengthTicks,
                               IAnimationPredicate<T> animationPredicate) {
        this.animatable = animatable;
        this.name = name;
        this.transitionLengthTicks = transitionLengthTicks;
        this.animationPredicate = animationPredicate;
        this.tickOffset = 0.0d;
    }

    /**
     * 实例化动画控制器，每个控制器同一时间只能播放一个动画 <br>
     * 你可以为一个实体附加多个动画控制器 <br>
     * 比如一个控制器控制实体大小，另一个控制移动，攻击等等
     *
     * @param animatable            实体
     * @param name                  动画控制器名称
     * @param transitionLengthTicks 动画过渡时间（tick）
     * @param easingtype            动画过渡插值类型，默认没有
     */
    public AnimationController(T animatable, String name, float transitionLengthTicks, EasingType easingtype,
                               IAnimationPredicate<T> animationPredicate) {
        this.animatable = animatable;
        this.name = name;
        this.transitionLengthTicks = transitionLengthTicks;
        this.easingType = easingtype;
        this.animationPredicate = animationPredicate;
        this.tickOffset = 0.0d;
    }

    /**
     * 实例化动画控制器，每个控制器同一时间只能播放一个动画 <br>
     * 你可以为一个实体附加多个动画控制器 <br>
     * 比如一个控制器控制实体大小，另一个控制移动，攻击等等
     *
     * @param animatable            实体
     * @param name                  动画控制器名称
     * @param transitionLengthTicks 动画过渡时间（tick）
     * @param customEasingMethod    自定义过渡插值类型，参数输入和输出均为 0-1
     *                              {@link com.elfmcys.yesstevemodel.geckolib3.core.easing.EasingManager}
     */
    public AnimationController(T animatable, String name, float transitionLengthTicks,
                               Double2DoubleFunction customEasingMethod, IAnimationPredicate<T> animationPredicate) {
        this.animatable = animatable;
        this.name = name;
        this.transitionLengthTicks = transitionLengthTicks;
        this.customEasingMethod = customEasingMethod;
        this.easingType = EasingType.CUSTOM;
        this.animationPredicate = animationPredicate;
        this.tickOffset = 0.0d;
    }

    public static void addModelFetcher(ModelFetcher<?> fetcher) {
        modelFetchers.add(fetcher);
    }

    public static void removeModelFetcher(ModelFetcher<?> fetcher) {
        Objects.requireNonNull(fetcher);
        modelFetchers.remove(fetcher);
    }

    /**
     * 此方法使用 AnimationBuilder 设置当前动画
     * 你可以每帧运行此方法，如果每次都传入相同的 AnimationBuilder，它将不会重新启动。
     * 此外，它还可以在动画状态之间平滑过渡
     */
    public void setAnimation(AnimationBuilder builder) {
        IAnimatableModel<T> model = getModel(this.animatable);
        if (model != null) {
            if (builder == null || builder.getRawAnimationList().size() == 0) {
                this.animationState = AnimationState.STOPPED;
            } else if (!builder.getRawAnimationList().equals(this.currentAnimationBuilder.getRawAnimationList()) || this.needsAnimationReload) {
                AtomicBoolean encounteredError = new AtomicBoolean(false);
                // 将动画名称列表转换为实际列表，并在此过程中跟踪循环布尔值
                LinkedList<Animation> animations = builder.getRawAnimationList().stream().map((rawAnimation) -> {
                    Animation animation = model.getAnimation(rawAnimation.animationName, animatable);
                    if (animation == null) {
                        TouhouLittleMaid.LOGGER.warn("Could not load animation: {}. Is it missing?", rawAnimation.animationName);
                        encounteredError.set(true);
                    }
                    if (animation != null && rawAnimation.loopType != null) {
                        animation.loop = rawAnimation.loopType;
                    }
                    return animation;
                }).collect(Collectors.toCollection(LinkedList::new));
                if (encounteredError.get()) {
                    return;
                }
                this.animationQueue = animations;
                this.currentAnimationBuilder = builder;
                // 下一个动画调用时，将其重置为 0
                this.shouldResetTick = true;
                this.animationState = AnimationState.TRANSITIONING;
                this.justStartedTransition = true;
                this.needsAnimationReload = false;
            }
        }
    }

    /**
     * 获取动画控制器名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 当前动画，可以为 null
     */
    @Nullable
    public Animation getCurrentAnimation() {
        return this.currentAnimation;
    }

    /**
     * 当前动画控制器状态
     */
    public AnimationState getAnimationState() {
        return this.animationState;
    }

    /**
     * 当前动画骨骼动画队列
     */
    public HashMap<String, BoneAnimationQueue> getBoneAnimationQueues() {
        return this.boneAnimationQueues;
    }

    /**
     * 注册 Sound Listener
     */
    public void registerSoundListener(ISoundListener<T> soundListener) {
        this.soundListener = soundListener;
    }

    /**
     * 注册 Particle Listener
     */
    public void registerParticleListener(IParticleListener<T> particleListener) {
        this.particleListener = particleListener;
    }

    /**
     * 注册 Custom Instruction Listener.
     */
    public void registerCustomInstructionListener(ICustomInstructionListener<T> customInstructionListener) {
        this.customInstructionListener = customInstructionListener;
    }

    /**
     * 此方法每帧调用一次，以便填充动画点队列并处理动画状态逻辑。
     *
     * @param tick                   当前 tick + 插值 tick
     * @param event                  动画测试事件
     * @param modelRendererList      所有的 AnimatedModelRender 列表
     * @param boneSnapshotCollection boneSnapshot 集合
     */
    public void process(final double tick, AnimationEvent<T> event, List<IBone> modelRendererList,
                        Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection, MolangParser parser,
                        boolean crashWhenCantFindBone) {
        parser.setValue("query.life_time", () -> tick / 20);
        if (this.currentAnimation != null) {
            IAnimatableModel<T> model = getModel(this.animatable);
            if (model != null) {
                Animation animation = model.getAnimation(currentAnimation.animationName, this.animatable);
                if (animation != null) {
                    ILoopType loop = this.currentAnimation.loop;
                    this.currentAnimation = animation;
                    this.currentAnimation.loop = loop;
                }
            }
        }
        createInitialQueues(modelRendererList);

        double adjustedTick = adjustTick(tick);
        // 过渡结束，重置 tick 并将动画设置为运行
        if (animationState == AnimationState.TRANSITIONING && adjustedTick >= this.transitionLengthTicks) {
            this.shouldResetTick = true;
            this.animationState = AnimationState.RUNNING;
            adjustedTick = adjustTick(tick);
        }
        assert adjustedTick >= 0 : "GeckoLib: Tick was less than zero";

        // 测试动画谓词
        PlayState playState = this.testAnimationPredicate(event);
        if (playState == PlayState.STOP || (this.currentAnimation == null && this.animationQueue.size() == 0)) {
            // 动画过渡到模型的初始状态
            this.animationState = AnimationState.STOPPED;
            this.justStopped = true;
            return;
        }

        if (this.justStartedTransition && (this.shouldResetTick || this.justStopped)) {
            this.justStopped = false;
            adjustedTick = adjustTick(tick);
        } else if (this.currentAnimation == null && this.animationQueue.size() != 0) {
            this.shouldResetTick = true;
            this.animationState = AnimationState.TRANSITIONING;
            this.justStartedTransition = true;
            this.needsAnimationReload = false;
            adjustedTick = adjustTick(tick);
        } else if (this.animationState != AnimationState.TRANSITIONING) {
            this.animationState = AnimationState.RUNNING;
        }

        // 处理过渡到其他动画（或仅开始一个动画）
        if (this.animationState == AnimationState.TRANSITIONING) {
            // 刚开始过渡，所以将当前动画设置为第一个
            if (adjustedTick == 0 || this.isJustStarting) {
                this.justStartedTransition = false;
                this.currentAnimation = animationQueue.poll();
                resetEventKeyFrames();
                saveSnapshotsForAnimation(this.currentAnimation, boneSnapshotCollection);
            }
            if (this.currentAnimation != null) {
                setAnimTime(parser, 0);
                for (BoneAnimation boneAnimation : this.currentAnimation.boneAnimations) {
                    BoneAnimationQueue boneAnimationQueue = this.boneAnimationQueues.get(boneAnimation.boneName);
                    BoneSnapshot boneSnapshot = this.boneSnapshots.get(boneAnimation.boneName);
                    Optional<IBone> first = Optional.empty();
                    for (IBone bone : modelRendererList) {
                        if (bone.getName().equals(boneAnimation.boneName)) {
                            first = Optional.of(bone);
                            break;
                        }
                    }
                    if (!first.isPresent()) {
                        if (crashWhenCantFindBone) {
                            throw new RuntimeException("Could not find bone: " + boneAnimation.boneName);
                        }
                        continue;
                    }
                    BoneSnapshot initialSnapshot = first.get().getInitialSnapshot();
                    // assert boneSnapshot != null : "Bone snapshot was null";
                    // fixme: Null?
                    if (boneSnapshot == null) {
                        continue;
                    }

                    VectorKeyFrameList<KeyFrame<IValue>> rotationKeyFrames = boneAnimation.rotationKeyFrames;
                    VectorKeyFrameList<KeyFrame<IValue>> positionKeyFrames = boneAnimation.positionKeyFrames;
                    VectorKeyFrameList<KeyFrame<IValue>> scaleKeyFrames = boneAnimation.scaleKeyFrames;

                    // 添加即将出现的动画的初始位置，以便模型转换到新动画的初始状态
                    if (!rotationKeyFrames.xKeyFrames.isEmpty()) {
                        AnimationPoint xPoint = getAnimationPointAtTick(rotationKeyFrames.xKeyFrames, 0, true, Axis.X);
                        AnimationPoint yPoint = getAnimationPointAtTick(rotationKeyFrames.yKeyFrames, 0, true, Axis.Y);
                        AnimationPoint zPoint = getAnimationPointAtTick(rotationKeyFrames.zKeyFrames, 0, true, Axis.Z);
                        boneAnimationQueue.rotationXQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.rotationValueX - initialSnapshot.rotationValueX,
                                xPoint.animationStartValue));
                        boneAnimationQueue.rotationYQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.rotationValueY - initialSnapshot.rotationValueY,
                                yPoint.animationStartValue));
                        boneAnimationQueue.rotationZQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.rotationValueZ - initialSnapshot.rotationValueZ,
                                zPoint.animationStartValue));
                    }

                    if (!positionKeyFrames.xKeyFrames.isEmpty()) {
                        AnimationPoint xPoint = getAnimationPointAtTick(positionKeyFrames.xKeyFrames, 0, false, Axis.X);
                        AnimationPoint yPoint = getAnimationPointAtTick(positionKeyFrames.yKeyFrames, 0, false, Axis.Y);
                        AnimationPoint zPoint = getAnimationPointAtTick(positionKeyFrames.zKeyFrames, 0, false, Axis.Z);
                        boneAnimationQueue.positionXQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.positionOffsetX, xPoint.animationStartValue));
                        boneAnimationQueue.positionYQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.positionOffsetY, yPoint.animationStartValue));
                        boneAnimationQueue.positionZQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.positionOffsetZ, zPoint.animationStartValue));
                    }

                    if (!scaleKeyFrames.xKeyFrames.isEmpty()) {
                        AnimationPoint xPoint = getAnimationPointAtTick(scaleKeyFrames.xKeyFrames, 0, false, Axis.X);
                        AnimationPoint yPoint = getAnimationPointAtTick(scaleKeyFrames.yKeyFrames, 0, false, Axis.Y);
                        AnimationPoint zPoint = getAnimationPointAtTick(scaleKeyFrames.zKeyFrames, 0, false, Axis.Z);
                        boneAnimationQueue.scaleXQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.scaleValueX, xPoint.animationStartValue));
                        boneAnimationQueue.scaleYQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.scaleValueY, yPoint.animationStartValue));
                        boneAnimationQueue.scaleZQueue().add(new AnimationPoint(null, adjustedTick, this.transitionLengthTicks,
                                boneSnapshot.scaleValueZ, zPoint.animationStartValue));
                    }
                }
            }
        } else if (getAnimationState() == AnimationState.RUNNING) {
            // 开始运行动画
            processCurrentAnimation(adjustedTick, tick, parser, crashWhenCantFindBone);
        }
    }

    private void setAnimTime(MolangParser parser, final double tick) {
        parser.setValue("query.anim_time", () -> tick / 20);
    }

    @SuppressWarnings("unchecked")
    private IAnimatableModel<T> getModel(T animatable) {
        for (ModelFetcher<?> modelFetcher : modelFetchers) {
            IAnimatableModel<T> model = (IAnimatableModel<T>) modelFetcher.apply(animatable);
            if (model != null) {
                return model;
            }
        }
        TouhouLittleMaid.LOGGER.warn("Could not find suitable model for animatable of type {}. Did you register a Model Fetcher?", animatable.getClass());
        return null;
    }

    protected PlayState testAnimationPredicate(AnimationEvent<T> event) {
        return this.animationPredicate.test(event);
    }

    // 在开始新的过渡时，将模型的初始旋转、位置和缩放存储为快照
    private void saveSnapshotsForAnimation(Animation animation, Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection) {
        for (Pair<IBone, BoneSnapshot> snapshot : boneSnapshotCollection.values()) {
            if (animation != null && animation.boneAnimations != null) {
                for (BoneAnimation boneAnimation : animation.boneAnimations) {
                    if (boneAnimation.boneName.equals(snapshot.getLeft().getName())) {
                        this.boneSnapshots.put(boneAnimation.boneName, new BoneSnapshot(snapshot.getRight()));
                        break;
                    }
                }
            }
        }
    }

    private void processCurrentAnimation(double tick, double actualTick, MolangParser parser, boolean crashWhenCantFindBone) {
        assert currentAnimation != null;
        // 如果动画已经结束了
        if (tick >= this.currentAnimation.animationLength) {
            resetEventKeyFrames();
            // 如果动画为循环播放，继续重头播放
            if (!this.currentAnimation.loop.isRepeatingAfterEnd()) {
                // 从队列中提取下一个动画
                Animation peek = this.animationQueue.peek();
                if (peek == null) {
                    // 没有动画了，那么停止
                    this.animationState = AnimationState.STOPPED;
                    return;
                } else {
                    // 否则，将状态设置为过渡并开始过渡到下一个动画为下一帧
                    this.animationState = AnimationState.TRANSITIONING;
                    this.shouldResetTick = true;
                    this.currentAnimation = this.animationQueue.peek();
                }
            } else {
                // 重置 tick，以便下一个动画从刻度 0 开始
                this.shouldResetTick = true;
                tick = adjustTick(actualTick);
            }
        }
        setAnimTime(parser, tick);

        // 循环遍历当前动画中的每个骨骼动画并处理值
        List<BoneAnimation> boneAnimations = currentAnimation.boneAnimations;
        for (BoneAnimation boneAnimation : boneAnimations) {
            BoneAnimationQueue boneAnimationQueue = boneAnimationQueues.get(boneAnimation.boneName);
            if (boneAnimationQueue == null) {
                if (crashWhenCantFindBone) {
                    throw new RuntimeException("Could not find bone: " + boneAnimation.boneName);
                }
                continue;
            }

            VectorKeyFrameList<KeyFrame<IValue>> rotationKeyFrames = boneAnimation.rotationKeyFrames;
            VectorKeyFrameList<KeyFrame<IValue>> positionKeyFrames = boneAnimation.positionKeyFrames;
            VectorKeyFrameList<KeyFrame<IValue>> scaleKeyFrames = boneAnimation.scaleKeyFrames;

            if (!rotationKeyFrames.xKeyFrames.isEmpty()) {
                boneAnimationQueue.rotationXQueue()
                        .add(getAnimationPointAtTick(rotationKeyFrames.xKeyFrames, tick, true, Axis.X));
                boneAnimationQueue.rotationYQueue()
                        .add(getAnimationPointAtTick(rotationKeyFrames.yKeyFrames, tick, true, Axis.Y));
                boneAnimationQueue.rotationZQueue()
                        .add(getAnimationPointAtTick(rotationKeyFrames.zKeyFrames, tick, true, Axis.Z));
            }

            if (!positionKeyFrames.xKeyFrames.isEmpty()) {
                boneAnimationQueue.positionXQueue()
                        .add(getAnimationPointAtTick(positionKeyFrames.xKeyFrames, tick, false, Axis.X));
                boneAnimationQueue.positionYQueue()
                        .add(getAnimationPointAtTick(positionKeyFrames.yKeyFrames, tick, false, Axis.Y));
                boneAnimationQueue.positionZQueue()
                        .add(getAnimationPointAtTick(positionKeyFrames.zKeyFrames, tick, false, Axis.Z));
            }

            if (!scaleKeyFrames.xKeyFrames.isEmpty()) {
                boneAnimationQueue.scaleXQueue()
                        .add(getAnimationPointAtTick(scaleKeyFrames.xKeyFrames, tick, false, Axis.X));
                boneAnimationQueue.scaleYQueue()
                        .add(getAnimationPointAtTick(scaleKeyFrames.yKeyFrames, tick, false, Axis.Y));
                boneAnimationQueue.scaleZQueue()
                        .add(getAnimationPointAtTick(scaleKeyFrames.zKeyFrames, tick, false, Axis.Z));
            }
        }

        if (this.soundListener != null || this.particleListener != null || this.customInstructionListener != null) {
            for (EventKeyFrame<String> soundKeyFrame : this.currentAnimation.soundKeyFrames) {
                if (!this.executedKeyFrames.contains(soundKeyFrame) && tick >= soundKeyFrame.getStartTick()) {
                    SoundKeyframeEvent<T> event = new SoundKeyframeEvent<>(this.animatable, tick, soundKeyFrame.getEventData(), this);
                    this.soundListener.playSound(event);
                    this.executedKeyFrames.add(soundKeyFrame);
                }
            }
            for (ParticleEventKeyFrame particleEventKeyFrame : this.currentAnimation.particleKeyFrames) {
                if (!this.executedKeyFrames.contains(particleEventKeyFrame) && tick >= particleEventKeyFrame.getStartTick()) {
                    ParticleKeyFrameEvent<T> event = new ParticleKeyFrameEvent<>(this.animatable, tick, particleEventKeyFrame.effect, particleEventKeyFrame.locator, particleEventKeyFrame.script, this);
                    this.particleListener.summonParticle(event);
                    this.executedKeyFrames.add(particleEventKeyFrame);
                }
            }
            for (EventKeyFrame<String> customInstructionKeyFrame : currentAnimation.customInstructionKeyframes) {
                if (!this.executedKeyFrames.contains(customInstructionKeyFrame) && tick >= customInstructionKeyFrame.getStartTick()) {
                    CustomInstructionKeyframeEvent<T> event = new CustomInstructionKeyframeEvent<>(this.animatable, tick, customInstructionKeyFrame.getEventData(), this);
                    this.customInstructionListener.executeInstruction(event);
                    this.executedKeyFrames.add(customInstructionKeyFrame);
                }
            }
        }

        if (this.transitionLengthTicks == 0 && shouldResetTick && this.animationState == AnimationState.TRANSITIONING) {
            this.currentAnimation = animationQueue.poll();
        }
    }

    // 填充所有初始动画点队列
    private void createInitialQueues(List<IBone> modelRendererList) {
        this.boneAnimationQueues.clear();
        for (IBone modelRenderer : modelRendererList) {
            this.boneAnimationQueues.put(modelRenderer.getName(), new BoneAnimationQueue(modelRenderer));
        }
    }

    // 在新动画开始、过渡开始或者其他情况下重置 tick
    public double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (getAnimationState() == AnimationState.TRANSITIONING) {
                this.tickOffset = tick;
            } else if (getAnimationState() == AnimationState.RUNNING) {
                this.tickOffset = tick;
            }
            this.shouldResetTick = false;
            return 0;
        } else {
            return this.animationSpeed * Math.max(tick - this.tickOffset, 0.0D);
        }
    }

    // 将关键帧位置转换为动画点
    private AnimationPoint getAnimationPointAtTick(List<KeyFrame<IValue>> frames, double tick, boolean isRotation, Axis axis) {
        KeyFrameLocation<KeyFrame<IValue>> location = getCurrentKeyFrameLocation(frames, tick);
        KeyFrame<IValue> currentFrame = location.currentFrame;
        double startValue = currentFrame.getStartValue().get();
        double endValue = currentFrame.getEndValue().get();

        if (isRotation) {
            if (!(currentFrame.getStartValue() instanceof ConstantValue)) {
                startValue = Math.toRadians(startValue);
                if (axis == Axis.X || axis == Axis.Y) {
                    startValue *= -1;
                }
            }
            if (!(currentFrame.getEndValue() instanceof ConstantValue)) {
                endValue = Math.toRadians(endValue);
                if (axis == Axis.X || axis == Axis.Y) {
                    endValue *= -1;
                }
            }
        }
        return new AnimationPoint(currentFrame, location.currentTick, currentFrame.getLength(), startValue, endValue);
    }

    /**
     * 返回当前关键帧，以及上一个关键帧耗时
     **/
    private KeyFrameLocation<KeyFrame<IValue>> getCurrentKeyFrameLocation(List<KeyFrame<IValue>> frames, double ageInTicks) {
        double totalTimeTracker = 0;
        for (KeyFrame<IValue> frame : frames) {
            totalTimeTracker += frame.getLength();
            if (totalTimeTracker > ageInTicks) {
                double tick = (ageInTicks - (totalTimeTracker - frame.getLength()));
                return new KeyFrameLocation<>(frame, tick);
            }
        }
        return new KeyFrameLocation<>(frames.get(frames.size() - 1), ageInTicks);
    }

    private void resetEventKeyFrames() {
        this.executedKeyFrames.clear();
    }

    public void markNeedsReload() {
        this.needsAnimationReload = true;
    }

    public void clearAnimationCache() {
        this.currentAnimationBuilder = new AnimationBuilder();
    }

    public double getAnimationSpeed() {
        return this.animationSpeed;
    }

    public void setAnimationSpeed(double animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    /**
     * 每个 AnimationController 每个关键帧都会运行一次 AnimationPredicate
     * test 方法就是你改变动画、停止动画、重置的地方
     */
    @FunctionalInterface
    public interface IAnimationPredicate<P extends IAnimatable> {
        PlayState test(AnimationEvent<P> event);
    }

    @FunctionalInterface
    public interface ISoundListener<A extends IAnimatable> {
        void playSound(SoundKeyframeEvent<A> event);
    }

    @FunctionalInterface
    public interface IParticleListener<A extends IAnimatable> {
        void summonParticle(ParticleKeyFrameEvent<A> event);
    }

    @FunctionalInterface
    public interface ICustomInstructionListener<A extends IAnimatable> {
        void executeInstruction(CustomInstructionKeyframeEvent<A> event);
    }

    @FunctionalInterface
    public interface ModelFetcher<T> extends Function<IAnimatable, IAnimatableModel<T>> {
    }
}