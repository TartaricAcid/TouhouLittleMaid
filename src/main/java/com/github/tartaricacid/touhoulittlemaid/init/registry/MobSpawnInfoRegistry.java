package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION;


@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    private static MobSpawnSettings.SpawnerData SPAWNER_DATA;

    @SubscribeEvent
    public static void addMobSpawnInfo(LevelEvent.PotentialSpawns event) {
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

    private static boolean dimensionIsOkay(ResourceLocation id) {
        return !MAID_FAIRY_BLACKLIST_DIMENSION.get().contains(id.toString());
    }
}
