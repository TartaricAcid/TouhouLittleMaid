package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class SingletonAnimationFactory extends AnimationFactory {
    private final Cache<Integer, AnimationData> animationDataMap = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).build();

    public SingletonAnimationFactory(IAnimatable animatable) {
        super(animatable);
    }

    @Override
    public AnimationData getOrCreateAnimationData(int uniqueID) {
        try {
            return this.animationDataMap.get(uniqueID, () -> {
                AnimationData data = new AnimationData();
                this.animatable.registerControllers(data);
                this.animationDataMap.put(uniqueID, data);
                return data;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}