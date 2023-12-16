package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        if (event.getLevel() instanceof Level level) {
            ResourceLocation dimension = level.dimension().location();
            if (dimensionIsOkay(dimension) && event.getMobCategory() == MobCategory.MONSTER) {
                List<MobSpawnSettings.SpawnerData> spawnerData = event.getSpawnerDataList();
                boolean canZombieSpawn = spawnerData.stream().anyMatch(data -> data.type.equals(EntityType.ZOMBIE));
                if (SPAWNER_DATA == null) {
                    SPAWNER_DATA = new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 4);
                }
                if (canZombieSpawn) {
                    event.addSpawnerData(SPAWNER_DATA);
                }
            }
        }
    }
}
