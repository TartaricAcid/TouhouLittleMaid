package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        event.getSpawns().addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 6));
    }
}
