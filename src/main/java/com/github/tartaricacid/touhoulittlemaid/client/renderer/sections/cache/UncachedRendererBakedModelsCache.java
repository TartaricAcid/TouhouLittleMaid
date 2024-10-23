package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @author Argon4W
 */
public class UncachedRendererBakedModelsCache implements RendererBakedModelsCache {
    @Override
    public BakedModel getTransformedModel(BakedModel model, PoseStack poseStack) {
        return getTransformedModel(model, new Transformation(poseStack.last().pose()));
    }

    @Override
    public BakedModel getTransformedModel(BakedModel model, Transformation transformation) {
        return new DynamicTransformedBakedModel(model, transformation, this);
    }

    @Override
    public <E extends Entity> CachedEntityModel getEntityModel(EntityType<? extends E> entityType, Level level) {
        return CachedEntityModel.create(entityType, level);
    }

    @Override
    public <E extends Entity> CachedEntityModel getEntityModel(E entity, ResourceLocation cacheLocation) {
        return CachedEntityModel.create(entity);
    }

    @Override
    public int getSize() {
        return 0;
    }
}
