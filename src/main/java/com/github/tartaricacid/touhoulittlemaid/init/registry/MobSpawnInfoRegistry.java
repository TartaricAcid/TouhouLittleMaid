package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnInfo.Spawners SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        List<MobSpawnInfo.Spawners> spawnerData = event.getSpawns().getSpawner(EntityClassification.MONSTER);
        boolean canZombieSpawn = spawnerData.stream().anyMatch(data -> data.type.equals(EntityType.ZOMBIE));
        if (SPAWNER_DATA == null) {
            SPAWNER_DATA = new MobSpawnInfo.Spawners(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 4);
        }
        if (canZombieSpawn) {
            event.getSpawns().addSpawn(EntityClassification.MONSTER, SPAWNER_DATA);
        }
    }
}
