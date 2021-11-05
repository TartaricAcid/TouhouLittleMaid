package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class BiomeCacheUtil {
    private static final Cache<Entity, Biome> BIOME_CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build();

    public static Biome getCacheBiome(Entity entity) {
        try {
            return BIOME_CACHE.get(entity, () -> entity.level.getBiome(entity.blockPosition()));
        } catch (ExecutionException ignore) {
            return entity.level.getBiome(entity.blockPosition());
        }
    }
}
