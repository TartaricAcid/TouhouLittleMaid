package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class BiomeCacheUtil {
    private static final Cache<Entity, Biome> BIOME_CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build();

    public static Biome getCacheBiome(Entity entity) {
        try {
            return BIOME_CACHE.get(entity, () -> entity.level.getBiome(entity.blockPosition()).value());
        } catch (ExecutionException ignore) {
            return entity.level.getBiome(entity.blockPosition()).value();
        }
    }
}
