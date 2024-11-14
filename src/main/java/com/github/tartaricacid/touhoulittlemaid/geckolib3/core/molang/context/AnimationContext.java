package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context;

import com.github.tartaricacid.touhoulittlemaid.capability.GeckoMaidEntityCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.AnimatableEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationControllerContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.IForeignVariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.IScopedVariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.ITempVariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.storage.VariableStorage;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.data.EntityModelData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Mob;

import java.util.Random;

public class AnimationContext<TEntity> implements IContext<TEntity> {
    protected final TEntity entity;
    protected final AnimatableEntity<?> instance;
    protected final AnimationEvent<?> animationEvent;
    protected final EntityModelData data;

    protected AnimationControllerContext animationControllerContext;
    protected Random random;
    protected VariableStorage storage;
    protected IForeignVariableStorage foreignStorage;

    public AnimationContext(TEntity entity, AnimatableEntity<?> instance, AnimationEvent<?> animationEvent, EntityModelData data) {
        this.entity = entity;
        this.instance = instance;
        this.animationEvent = animationEvent;
        this.data = data;
    }

    private AnimationContext(TEntity entity, AnimatableEntity<?> instance, AnimationEvent<?> animationEvent, EntityModelData data, AnimationControllerContext animationControllerContext, Random random, VariableStorage storage) {
        this.entity = entity;
        this.instance = instance;
        this.animationEvent = animationEvent;
        this.data = data;
        this.animationControllerContext = animationControllerContext;
        this.random = random;
        this.storage = storage;
        if (entity instanceof Mob mob) {
            mob.getCapability(GeckoMaidEntityCapabilityProvider.CAP)
                    .ifPresent(cap -> foreignStorage = cap.getAnimationProcessor().getPublicVariableStorage());
        }
    }

    @Override
    public AnimationEvent<?> animationEvent() {
        return animationEvent;
    }

    @Override
    public AnimatableEntity<?> geoInstance() {
        return instance;
    }

    @Override
    public EntityModelData data() {
        return data;
    }

    @Override
    public AnimationControllerContext animationControllerContext() {
        return animationControllerContext;
    }

    @Override
    public Random random() {
        return random;
    }

    @Override
    public TEntity entity() {
        return entity;
    }

    @Override
    public Minecraft mc() {
        return Minecraft.getInstance();
    }

    @Override
    public ClientLevel level() {
        Minecraft mc = mc();
        if (mc != null) {
            return mc.level;
        } else {
            return null;
        }
    }

    @Override
    public <TChild> IContext<TChild> createChild(TChild child) {
        return new AnimationContext<>(child, instance, animationEvent, data, animationControllerContext, random, storage);
    }

    @Override
    public ITempVariableStorage tempStorage() {
        return storage;
    }

    @Override
    public IScopedVariableStorage scopedStorage() {
        return storage;
    }

    @Override
    public IForeignVariableStorage foreignStorage() {
        return foreignStorage;
    }

    public void setAnimationControllerContext(AnimationControllerContext animationControllerContext) {
        this.animationControllerContext = animationControllerContext;
    }

    public void setStorage(VariableStorage storage) {
        this.storage = storage;
        this.foreignStorage = storage;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
