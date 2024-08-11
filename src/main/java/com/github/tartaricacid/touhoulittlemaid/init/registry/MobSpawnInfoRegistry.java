package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION;

@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnSettings.SpawnerData SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        int spawnProbability = MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get();
        if (spawnProbability <= 0) {
            // 优先判断等于 0 的情况，减少性能消耗
            return;
        }
        List<MobSpawnSettings.SpawnerData> spawnerData = event.getSpawns().getSpawner(MobCategory.MONSTER);
        boolean canZombieSpawn = spawnerData.stream().anyMatch(data -> data.type.equals(EntityType.ZOMBIE));
        if (SPAWNER_DATA == null) {
            SPAWNER_DATA = new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), spawnProbability, 2, 4);
        }
        if (canZombieSpawn) {
            event.getSpawns().addSpawn(MobCategory.MONSTER, SPAWNER_DATA);
        }
    }

    private static boolean dimensionIsOkay(ResourceLocation id) {
        return !MAID_FAIRY_BLACKLIST_DIMENSION.get().contains(id.toString());
    }
}
