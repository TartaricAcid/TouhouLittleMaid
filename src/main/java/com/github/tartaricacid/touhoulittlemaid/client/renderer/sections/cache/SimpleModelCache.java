package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.SimpleBakedModelExtension;
import com.github.tartaricacid.touhoulittlemaid.util.EntryStreams;
import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Argon4W
 */
public class SimpleModelCache implements BakedModelCache {
    private final SimpleBakedModel model;
    private final Map<Transformation, BakedModel> modelCache;

    public SimpleModelCache(SimpleBakedModel model) {
        this.model = model;
        this.modelCache = new ConcurrentHashMap<>();
    }

    @Override
    public BakedModel getTransformedModel(Transformation transformation) {
        return modelCache.computeIfAbsent(transformation, transformation1 -> getTransformedModel(transformation1.getMatrix(), QuadTransformers.applying(transformation1)));
    }

    public BakedModel getTransformedModel(Matrix4f matrix4f, IQuadTransformer transformer) {
        return new SimpleBakedModel(model.unculledFaces.stream().map(transformer::process).toList(), model.culledFaces.entrySet().stream().map(EntryStreams.mapEntryValue(list -> list.stream().map(transformer::process).toList())).collect(EntryStreams.collect()), model.useAmbientOcclusion(), model.usesBlockLight(), model.isGui3d(), model.getParticleIcon(), model.getTransforms(), model.getOverrides(), model instanceof SimpleBakedModelExtension extension ? extension.eyelib$getRenderTypeGroup() : RenderTypeGroup.EMPTY);
    }

    @Override
    public int size() {
        return modelCache.size();
    }
}
