package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION;


@EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnSettings.SpawnerData SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(LevelEvent.PotentialSpawns event) {
        if (event.getLevel() instanceof Level level) {
            int spawnProbability = MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get();
            if (spawnProbability <= 0) {
                // 优先判断等于 0 的情况，减少性能消耗
                return;
            }
            ResourceLocation dimension = level.dimension().location();
            if (event.getMobCategory() == MobCategory.MONSTER && dimensionIsOkay(dimension)) {
                List<MobSpawnSettings.SpawnerData> spawnerData = event.getSpawnerDataList();
                boolean canZombieSpawn = spawnerData.stream().anyMatch(data -> data.type.equals(EntityType.ZOMBIE));
                if (SPAWNER_DATA == null) {
                    SPAWNER_DATA = new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), spawnProbability, 2, 4);
                }
                if (canZombieSpawn) {
                    event.addSpawnerData(SPAWNER_DATA);
                }
            }
        }
    }

    private static boolean dimensionIsOkay(ResourceLocation id) {
        return !MAID_FAIRY_BLACKLIST_DIMENSION.get().contains(id.toString());
    }
}
