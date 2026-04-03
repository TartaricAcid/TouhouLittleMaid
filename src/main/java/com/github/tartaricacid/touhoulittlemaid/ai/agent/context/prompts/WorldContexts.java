package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.prompts;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

public final class WorldContexts {
    public static final String CATEGORY = "world";
    private static final String SUMMARY = "Time, weather, dimension, and biome around";

    private WorldContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, true);
        register.registerContext(CATEGORY, new GameTimeContext());
        register.registerContext(CATEGORY, new WeatherContext());
        register.registerContext(CATEGORY, new DimensionContext());
        register.registerContext(CATEGORY, new BiomeContext());
    }

    private static final class GameTimeContext extends AbstractMaidContext {
        private GameTimeContext() {
            super("game_time", "Time");
        }

        @Override
        public String getValue(EntityMaid maid) {
            long time = maid.level.getDayTime();
            long hours = (time / 1000 + 6) % 24;
            long minutes = (time % 1000) / (50 / 3);
            return TIME_FORMAT.formatted(hours, minutes);
        }
    }

    private static final class WeatherContext extends AbstractMaidContext {
        private WeatherContext() {
            super("weather", "Weather");
        }

        @Override
        public String getValue(EntityMaid maid) {
            Level level = maid.level;
            if (level.isThundering()) {
                return THUNDERING;
            }
            if (level.isRaining()) {
                return RAINING;
            }
            return SUNNY;
        }
    }

    private static final class DimensionContext extends AbstractMaidContext {
        private DimensionContext() {
            super("dimension", "Dimension");
        }

        @Override
        public String getValue(EntityMaid maid) {
            ResourceKey<Level> dimension = maid.level.dimension();
            if (dimension == Level.OVERWORLD) {
                return OVERWORLD;
            }
            if (dimension == Level.NETHER) {
                return NETHER;
            }
            if (dimension == Level.END) {
                return END;
            }
            return dimension.location().toString();
        }
    }

    private static final class BiomeContext extends AbstractMaidContext {
        private BiomeContext() {
            super("biome", "Biome");
        }

        @Override
        public String getValue(EntityMaid maid) {
            Biome biome = maid.level.getBiome(maid.blockPosition()).value();
            ResourceLocation key = maid.level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
            return key == null ? UNKNOWN_BIOME : key.toString();
        }
    }
}
