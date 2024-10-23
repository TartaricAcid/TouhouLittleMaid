package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Argon4W
 */
public class DefaultRendererBakedModelsCache implements RendererBakedModelsCache {
    private final Map<BakedModel, BakedModelCache> modelCache;
    private final Map<EntityModelCacheKey<?>, CachedEntityModel> entityModelCache;

    public DefaultRendererBakedModelsCache() {
        this.modelCache = new ConcurrentHashMap<>();
        this.entityModelCache = new ConcurrentHashMap<>();
    }

    @Override
    public BakedModel getTransformedModel(BakedModel model, PoseStack poseStack) {
        return getTransformedModel(model, new Transformation(poseStack.last().pose()));
    }

    @Override
    public BakedModel getTransformedModel(BakedModel model, Transformation transformation) {
        return modelCache.compute(model, (model1, cache) -> cache == null ? createModelCache(model) : (cache.size() > 32 ? new DynamicModelCache(model1, this) : cache)).getTransformedModel(transformation);
    }

    @Override
    public <E extends Entity> CachedEntityModel getEntityModel(EntityType<? extends E> entityType, Level level) {
        return entityModelCache.computeIfAbsent(new EntityModelCacheKey<>(entityType), cacheKey -> CachedEntityModel.create(cacheKey.entityType(), level));
    }

    @Override
    public <E extends Entity> CachedEntityModel getEntityModel(E entity, ResourceLocation cacheLocation) {
        return entityModelCache.computeIfAbsent(new EntityModelCacheKey<>(entity.getType(), cacheLocation), cacheKey -> CachedEntityModel.create(entity));
    }

    @Override
    public int getSize() {
        return modelCache.values().stream().mapToInt(BakedModelCache::size).sum() + entityModelCache.size();
    }

    public BakedModelCache createModelCache(BakedModel model) {
        if (model instanceof SimpleBakedModel simple) {
            return new SimpleModelCache(simple);
        }

        if (model instanceof CachedEntityModel entity) {
            return new EntityModelCache(entity);
        }

        return new DynamicModelCache(model, this);
    }
}
