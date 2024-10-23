package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.github.tartaricacid.touhoulittlemaid.util.EntryStreams;
import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Argon4W
 */
public class EntityModelCache implements BakedModelCache {
    private final CachedEntityModel model;
    private final Map<Transformation, BakedModel> modelCache;

    public EntityModelCache(CachedEntityModel model) {
        this.model = model;
        this.modelCache = new ConcurrentHashMap<>();
    }

    @Override
    public BakedModel getTransformedModel(Transformation transformation) {
        return modelCache.computeIfAbsent(transformation, transformation1 -> getTransformedModel(QuadTransformers.applying(transformation1)));
    }

    public BakedModel getTransformedModel(IQuadTransformer transformer) {
        return new CachedEntityModel(model.cachedQuads().entrySet().stream().map(EntryStreams.mapEntryValue(list -> list.stream().map(transformer::process).toList())).collect(EntryStreams.collect()));
    }

    @Override
    public int size() {
        return modelCache.size();
    }
}
