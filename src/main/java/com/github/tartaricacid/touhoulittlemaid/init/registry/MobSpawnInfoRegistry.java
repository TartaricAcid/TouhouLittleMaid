package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnSettings.SpawnerData SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(LevelEvent.PotentialSpawns event) {
        Holder<Biome> biome = event.getLevel().getBiome(event.getPos());
        if (biomeIsOkay(biome) && event.getMobCategory() == MobCategory.MONSTER) {
            if (SPAWNER_DATA == null) {
                SPAWNER_DATA = new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 6);
            }
            event.addSpawnerData(SPAWNER_DATA);
        }
    }

    private static boolean biomeIsOkay(Holder<Biome> biome) {
        // TODO: 2021/10/17 添加配置文件，管控可生成的生物群系
        return !biome.is(Biomes.NETHER_WASTES) && !biome.is(Biomes.THE_END) && !biome.is(Biomes.THE_VOID) && !biome.is(Biomes.MUSHROOM_FIELDS);
    }
}
