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
public interface RendererBakedModelsCache {
    BakedModel getTransformedModel(BakedModel model, PoseStack poseStack);

    BakedModel getTransformedModel(BakedModel model, Transformation transformation);

    <E extends Entity> CachedEntityModel getEntityModel(EntityType<? extends E> entityType, Level level);

    <E extends Entity> CachedEntityModel getEntityModel(E entity, ResourceLocation cacheLocation);

    int getSize();
}
