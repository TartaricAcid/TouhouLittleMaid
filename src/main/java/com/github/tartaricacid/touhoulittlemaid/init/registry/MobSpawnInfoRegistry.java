package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig.MAID_FAIRY_BLACKLIST_BIOME;


@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnSettings.SpawnerData SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        if (biomeIsOkay(event.getCategory())) {
            List<MobSpawnSettings.SpawnerData> spawnerData = event.getSpawns().getSpawner(MobCategory.MONSTER);
            boolean canZombieSpawn = spawnerData.stream().anyMatch(data -> data.type.equals(EntityType.ZOMBIE));
            if (SPAWNER_DATA == null) {
                SPAWNER_DATA = new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 4);
            }
            if (canZombieSpawn) {
                event.getSpawns().addSpawn(MobCategory.MONSTER, SPAWNER_DATA);
            }
        }
    }

    private static boolean biomeIsOkay(Biome.BiomeCategory category) {
        return !MAID_FAIRY_BLACKLIST_BIOME.get().contains(category.getName());
    }
}