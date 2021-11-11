package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public final class SoundUtil {
    private static final long MORNING_START = 0;
    private static final long MORNING_END = 3000;
    private static final long EVENING_START = 12000;
    private static final long EVENING_END = 15000;
    private static final float LOW_TEMPERATURE = 0.15F;
    private static final float HIGH_TEMPERATURE = 0.95F;

    public static SoundEvent environmentSound(EntityMaid maid, SoundEvent fallback, float probability) {
        World world = maid.level;
        Random rand = maid.getRandom();
        BlockPos pos = maid.blockPosition();
        long dayTime = world.getDayTime();
        Biome biome = world.getBiome(pos);

        // 差不多早上 6:00 - 9:00
        if (rand.nextFloat() < probability && MORNING_START < dayTime && dayTime < MORNING_END) {
            return InitSounds.MAID_MORNING.get();
        }
        // 差不多下午 6:00 - 9:00
        if (rand.nextFloat() < probability && EVENING_START < dayTime && dayTime < EVENING_END) {
            return InitSounds.MAID_NIGHT.get();
        }
        if (rand.nextFloat() < probability && world.isRaining() && isRainBiome(biome, pos)) {
            return InitSounds.MAID_RAIN.get();
        }
        if (rand.nextFloat() < probability && world.isRaining() && isSnowyBiome(biome, pos)) {
            return InitSounds.MAID_SNOW.get();
        }
        if (rand.nextFloat() < probability && biome.getTemperature(pos) < LOW_TEMPERATURE) {
            return InitSounds.MAID_COLD.get();
        }
        if (rand.nextFloat() < probability && biome.getTemperature(pos) > HIGH_TEMPERATURE) {
            return InitSounds.MAID_HOT.get();
        }
        return fallback;
    }

    public static SoundEvent attackSound(EntityMaid maid, SoundEvent fallback, float probability) {
        Random rand = maid.getRandom();
        if (rand.nextFloat() < probability) {
            return InitSounds.MAID_FIND_TARGET.get();
        }
        return fallback;
    }

    private static boolean isRainBiome(Biome biome, BlockPos pos) {
        float temp = biome.getTemperature(pos);
        return biome.getPrecipitation() == Biome.RainType.RAIN && LOW_TEMPERATURE <= temp && temp <= HIGH_TEMPERATURE;
    }

    private static boolean isSnowyBiome(Biome biome, BlockPos pos) {
        return biome.getPrecipitation() == Biome.RainType.SNOW && biome.getTemperature(pos) < LOW_TEMPERATURE;
    }
}
