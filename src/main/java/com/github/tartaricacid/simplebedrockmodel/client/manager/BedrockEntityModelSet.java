package com.github.tartaricacid.simplebedrockmodel.client.manager;

import com.github.tartaricacid.simplebedrockmodel.SimpleBedrockModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

public class BedrockEntityModelSet<T extends AbstractBedrockEntityModel<? extends Entity>> extends SimplePreparableReloadListener<Void> {
    private Map<ResourceLocation, T> models = ImmutableMap.of();
    private Map<ResourceLocation, Function<InputStream, T>> knowLocations = Maps.newHashMap();

    void addModel(ResourceLocation location, Function<InputStream, T> function) {
        this.knowLocations.put(location, function);
    }

    void immutableKnowLocations() {
        this.knowLocations = ImmutableMap.copyOf(knowLocations);
    }

    @SuppressWarnings("removal")
    @Override
    protected Void prepare(ResourceManager manager, ProfilerFiller filler) {
        this.models = Maps.newHashMap();
        this.knowLocations.keySet().forEach(location -> {
            // 将 ID 转换成实际模型文件路径，默认是 <namespace>:models/<path>.json
            ResourceLocation path = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
            Function<InputStream, T> modelFunction = knowLocations.get(location);
            manager.getResource(path).ifPresentOrElse(model -> {
                SimpleBedrockModel.LOGGER.info("Loading bedrock model file: {}", path);
                try (InputStream stream = model.open()) {
                    this.models.put(location, modelFunction.apply(stream));
                } catch (IOException e) {
                    SimpleBedrockModel.LOGGER.error("Failed to load model file: {}", path, e);
                }
            }, () -> SimpleBedrockModel.LOGGER.error("Not found model file: {}", path));
        });
        return null;
    }

    @Override
    protected void apply(Void unused, ResourceManager manager, ProfilerFiller filler) {
        this.models = ImmutableMap.copyOf(models);
    }

    Map<ResourceLocation, ? extends AbstractBedrockEntityModel<? extends Entity>> getModels() {
        return models;
    }
}
