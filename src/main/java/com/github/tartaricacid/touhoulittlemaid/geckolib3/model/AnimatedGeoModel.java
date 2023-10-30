package com.github.tartaricacid.touhoulittlemaid.geckolib3.model;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatableModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.AnimationProcessor;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.exception.GeckoLibException;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.GeoModelProvider;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.IAnimatableModelProvider;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.mojang.blaze3d.Blaze3D;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AnimatedGeoModel<T extends IAnimatable> extends GeoModelProvider<T> implements IAnimatableModel<T>, IAnimatableModelProvider<T> {
    private final AnimationProcessor animationProcessor;
    private GeoModel currentModel;

    protected AnimatedGeoModel() {
        this.animationProcessor = new AnimationProcessor(this);
    }

    public void registerBone(GeoBone bone) {
        registerModelRenderer(bone);
        for (GeoBone childBone : bone.childBones) {
            registerBone(childBone);
        }
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        Minecraft mc = Minecraft.getInstance();
        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        AnimationEvent<T> predicate;
        double currentTick = animatable instanceof Entity livingEntity ? livingEntity.tickCount : getCurrentTick();

        if (manager.startTick == -1) {
            manager.startTick = currentTick + mc.getFrameTime();
        }

        if (!mc.isPaused() || manager.shouldPlayWhilePaused) {
            if (animatable instanceof LivingEntity) {
                manager.tick = currentTick + mc.getFrameTime();
                double gameTick = manager.tick;
                double deltaTicks = gameTick - this.lastGameTickTime;
                this.seekTime += deltaTicks;
                this.lastGameTickTime = gameTick;
                codeAnimations(animatable, instanceId, animationEvent);
            } else {
                manager.tick = currentTick - manager.startTick;
                double gameTick = manager.tick;
                double deltaTicks = gameTick - this.lastGameTickTime;
                this.seekTime += deltaTicks;
                this.lastGameTickTime = gameTick;
            }
        }

        predicate = animationEvent == null ? new AnimationEvent<T>(animatable, 0, 0, (float) (manager.tick - this.lastGameTickTime), false, Collections.emptyList()) : animationEvent;
        predicate.animationTick = this.seekTime;
        getAnimationProcessor().preAnimationSetup(predicate.getAnimatable(), this.seekTime);
        if (!getAnimationProcessor().getModelRendererList().isEmpty()) {
            getAnimationProcessor().tickAnimation(animatable, instanceId, this.seekTime, predicate, GeckoLibCache.getInstance().parser, this.shouldCrashOnMissing);
        }
    }

    public void codeAnimations(T entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
    }

    @Override
    public AnimationProcessor getAnimationProcessor() {
        return this.animationProcessor;
    }

    public void registerModelRenderer(IBone modelRenderer) {
        this.animationProcessor.registerModelRenderer(modelRenderer);
    }

    @Override
    public Animation getAnimation(String name, IAnimatable animatable) {
        AnimationFile animation = GeckoLibCache.getInstance().getAnimations().get(this.getAnimationFileLocation((T) animatable));
        if (animation == null) {
            throw new GeckoLibException(this.getAnimationFileLocation((T) animatable), "Could not find animation file. Please double check name.");
        }
        return animation.getAnimation(name);
    }

    @Override
    public GeoModel getModel(ResourceLocation location) {
        GeoModel model = super.getModel(location);
        if (model == null) {
            throw new GeckoLibException(location, "Could not find model. If you are getting this with a built mod, please just restart your game.");
        }
        if (model != this.currentModel) {
            this.animationProcessor.clearModelRendererList();
            this.currentModel = model;
            for (GeoBone bone : model.topLevelBones) {
                registerBone(bone);
            }
        }
        return model;
    }

    public GeoModel getCurrentModel() {
        return currentModel;
    }

    @Override
    public double getCurrentTick() {
        return Blaze3D.getTime() * 20;
    }
}
