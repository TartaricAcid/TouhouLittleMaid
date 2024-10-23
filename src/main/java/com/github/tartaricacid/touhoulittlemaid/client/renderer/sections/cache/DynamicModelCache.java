package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;

/**
 * @author Argon4W
 */
public record DynamicModelCache(BakedModel model, RendererBakedModelsCache cache) implements BakedModelCache {
    @Override
    public BakedModel getTransformedModel(Transformation transformation) {
        return new DynamicTransformedBakedModel(model, transformation, cache);
    }

    @Override
    public int size() {
        return 0;
    }
}
