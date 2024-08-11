package com.github.tartaricacid.touhoulittlemaid.geckolib3.core;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.AnimationProcessor;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.RateLimiter;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked,rawtypes")
public abstract class AnimatableEntity<E extends Entity> {
    private final AnimationData manager = new AnimationData();
    private final AnimationProcessor animationProcessor;
    private final RateLimiter rateLimiter;
    protected final E entity;
    private AnimatedGeoModel currentModel;

    private double seekTime;
    private double lastGameTickTime;

    public AnimatableEntity(E entity, int fps) {
        this.entity = entity;
        this.rateLimiter = new RateLimiter(fps);
        this.animationProcessor = new AnimationProcessor(this);
    }

    /**
     * 注册动画控制器
     */
    public void addAnimationController(AnimationController value) {
        this.manager.addAnimationController(value);
    }

    public AnimationData getAnimationData() {
        return manager;
    }

    public abstract ResourceLocation getModelLocation();

    public abstract ResourceLocation getTextureLocation();

    public boolean setCustomAnimations(@NotNull AnimationEvent<?> animationEvent) {
        if (!updateModel()) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        double currentTick = getCurrentTick(animationEvent);

        if (this.manager.startTick == -1) {
            this.manager.startTick = currentTick;
        }

        if (!mc.isPaused() || this.manager.shouldPlayWhilePaused) {
            this.manager.tick = currentTick;
            double gameTick = manager.tick;
            double deltaTicks = gameTick - this.lastGameTickTime;
            this.seekTime += deltaTicks;
            this.lastGameTickTime = gameTick;
            codeAnimations(animationEvent);
        }

        animationEvent.animationTick = this.seekTime;
        this.animationProcessor.preAnimationSetup(this.seekTime);
        if (this.animationProcessor.isModelEmpty()) {
            return false;
        }
        if (!forceUpdate(animationEvent) && !this.rateLimiter.request((float) this.seekTime * 20)) {
            return false;
        }

        this.animationProcessor.tickAnimation(this.seekTime, animationEvent, GeckoLibCache.getInstance().parser);
        return true;
    }

    public void codeAnimations(AnimationEvent<?> customPredicate) {
    }

    public AnimationProcessor getAnimationProcessor() {
        return this.animationProcessor;
    }

    @Nullable
    public Animation getAnimation(String name) {
        AnimationFile animation = GeckoLibCache.getInstance().getAnimations().get(getAnimationFileLocation());
        if (animation == null) {
            TouhouLittleMaid.LOGGER.debug("{}: Could not find animation file. Please double check name.", this.getAnimationFileLocation());
            return null;
        }
        return animation.getAnimation(name);
    }

    public abstract ResourceLocation getAnimationFileLocation();

    public boolean updateModel() {
        var model = GeckoLibCache.getInstance().getGeoModels().get(getModelLocation());
        if (model == null) {
            return false;
        }
        if (this.currentModel == null || model != this.currentModel.geoModel()) {
            this.currentModel = new AnimatedGeoModel(model);
            this.animationProcessor.updateModel(this.currentModel.bones().values());
        }
        return true;
    }

    public AnimatedGeoModel getCurrentModel() {
        return currentModel;
    }

    public double getCurrentTick(AnimationEvent<?> animationEvent) {
        return this.entity.tickCount + animationEvent.getPartialTick();
    }

    public void setMolangQueries(double seekTime) {
    }

    /**
     * 无需确保幂等
     */
    protected boolean forceUpdate(AnimationEvent<?> animationEvent) {
        return false;
    }

    public E getEntity() {
        return entity;
    }
}
