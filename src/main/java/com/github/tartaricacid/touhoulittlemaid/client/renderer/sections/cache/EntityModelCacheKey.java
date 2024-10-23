package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * @author Argon4W
 */
public record EntityModelCacheKey<E extends Entity>(EntityType<? extends E> entityType, ResourceLocation cacheLocation) {
    public EntityModelCacheKey(EntityType<? extends E> entityType) {
        this(entityType, BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }
}